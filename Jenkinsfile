pipeline {
  agent any

  tools {
      // Install the Maven version configured as "M3" and add it to the path.
      maven "M3"
      nodejs "NodeJS"
  }

  environment {
    GIT_CREDS = credentials('github_access_token')
    ADMIN_TOKEN = credentials('admin_token')
  }


  stages {
    stage('Build project') {
      steps {
        parallel (
          "Spring": {
            script {
              FAILED_STAGE = env.STAGE_NAME+" - maven"
              sh 'mvn -B -Dmaven.test.skip=true clean package'
            }
          },
          "React": {
            script {
              FAILED_STAGE = env.STAGE_NAME+" - npm"
              sh 'CI=false npm --prefix ./React/rest-api install'
              sh 'CI=false npm --prefix ./React/rest-api run build'
              sh 'cp -r ./React/rest-api/build ./'
              sh 'find ./build/ -printf "%P\n" -type f -o -type l -o -type d | tar -czf build.tar.gz --no-recursion -C ./build/ -T -'
            }
          }
        )
      }
    }

    stage('Tests') {
      steps {
        parallel (
          "Unit tests": {
            script {
              FAILED_STAGE = env.STAGE_NAME
              sh "mvn -B --file pom.xml -Dmaven.test.failure.ignore=true -Dspring.profiles.active=junit test"
            }
          },
          "Jacoco test": {
            script{
              FAILED_STAGE = env.STAGE_NAME
              sh 'mvn clean org.jacoco:jacoco-maven-plugin:0.8.10:prepare-agent verify org.jacoco:jacoco-maven-plugin:0.8.10:report'
            }
          }
        )
      }
    }

    stage('SonarQube scan') {
      steps{
        withSonarQubeEnv(credentialsId: 'sonarqube global', installationName: 'sonarQube') { // You can override the credential to be used
          sh "mvn -B --file pom.xml -Dmaven.test.skip=true sonar:sonar"
          sh 'node React/rest-api/sonarqube-scanner.js'
        }
      }
    }

    stage('Build Dockerfile') {
      when {
        expression {
          env.GIT_BRANCH == "origin/master"
        }
        triggeredBy cause: "UserIdCause"
      }
      steps{
        parallel (
          frontend: {
            script {
              FAILED_STAGE = "build-frontend"
              echo env.GIT_BRANCH
              dockerImage = docker.build("gaborka98/rest-fe:latest", "-f Dockerfile-react .")
            }
          },
          backend: {
            script {
              FAILED_STAGE = "build-backend"
              dockerImage = docker.build("gaborka98/rest-be:latest", "-f Dockerfile-spring .")
            }
          }
        )
      }
    }
    stage('Deploy') {
      when {
        expression {
          env.GIT_BRANCH == "origin/master"
        }
        triggeredBy cause: "UserIdCause"
      }
      steps {
        script {
            FAILED_STAGE = env.STAGE_NAME
            sh "docker run -d --restart unless-stopped -p 8102:80 --name rest-page gaborka98/rest-fe:latest"
            sh "docker run -d --restart unless-stopped -p 8103:8080 --name rest-be -m 75m gaborka98/rest-be:latest"
        }
      }
    }
    stage('JavaDoc') {
      when {
        expression {
          env.GIT_BRANCH == "origin/master"
        }
        triggeredBy cause: "UserIdCause"
      }
      steps {
        script {
          sh 'mvn javadoc:javadoc'
          sh('git subtree push --prefix target/site/apidocs https://$GIT_CREDS_USR:$GIT_CREDS_PSW@github.com/AgroCorp/spring_rest.git gh-pages')
        }
      }
    }
}
  post {
    always{
      cleanWs(cleanWhenNotBuilt: false,
                      deleteDirs: true,
                      disableDeferredWipeout: true,
                      notFailBuild: true,
                      patterns: [[pattern: '**/target/surefire-reports/*', type: 'EXCLUDE'],
                      [pattern:'**/target/jacoco.exec']])
    }
    success {
        junit '**/target/surefire-reports/TEST-*.xml'
        jacoco(
            execPattern: '**/target/*.exec',
            classPattern: '**/target/classes',
            sourcePattern: '**/src/main'
        )
        archiveArtifacts 'target/*.jar'
        archiveArtifacts 'build.tar.gz'
  }
    failure {
        emailext body: "<b>Error in build</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br>Stage: ${FAILED_STAGE} <br> URL to build: ${env.BUILD_URL}",
        from: 'jenkins@sativus.space',
        subject: "ERROR CI: Project name -> ${env.JOB_NAME}", to: "gaborka812@gmail.com", attachLog: true;
    }
  }
}
