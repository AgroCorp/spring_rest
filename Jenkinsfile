pipeline {
  agent any

  tools {
      // Install the Maven version configured as "M3" and add it to the path.
      maven "M3"
  }
  stages {
    stage('Checkout Scm') {
      steps {
        git 'https://github.com/AgroCorp/spring_rest.git'
      }
    }

    stage('Build project') {
        steps {
            sh 'mvn -B -Dmaven.test.skip=true clean package'
        }
    }

    stage('SonarQube scan') {
        steps {
            sh "mvn -B --file pom.xml -Dmaven.test.skip=true clean verify sonar:sonar"
        }
    }

    stage('Unit tests') {
        steps {
            sh "mvn -B --file pom.xml -Dmaven.test.failure.ignore=true test"
        }
    }

//     stage('Build image') {
//         steps{
//           parallel (
//             frontend: {
//               script {
//                 FAILED_STAGE = env.STAGE_NAME+"-frontend"
//                 dockerImage = docker.build("gaborka98/rest-fe:latest", "-f Dockerfile-react")
//               }
//             },
//             backend: {
//               script {
//                 FAILED_STAGE = env.STAGE_NAME+"-backend"
//                 dockerImage = docker.build("gaborka98/rest-be:latest", "-f Dockerfile-spring .")
//               }
//             }
//           )
//         }
//     }
//
//     stage('Deploy') {
//         steps {
//             script {
//                 FAILED_STAGE = env.STAGE_NAME
//                 sh "docker container stop wedding-page"
//                 sh "docker container rm wedding-page"
//                 sh "docker run -d --restart unless-stopped -p 8102:80 -m 72m --name wedding-page gaborka98/rest-fe:latest"
//
//                 sh "docker container stop wedding-be"
//                 sh "docker container rm wedding-be"
//                 sh "docker run -d --restart unless-stopped -p 8103:3001 --name wedding-be gaborka98/rest-be:latest"
//             }
//         }
//     }
}
  post {
    success {
  //      junit '**/target/surefire-reports/TEST-*.xml'
        archiveArtifacts 'target/*.jar'
  }
    failure {
        emailext body: "<b>Error in build</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br>Stage: ${FAILED_STAGE} <br> URL to build: ${env.BUILD_URL}",
        from: 'jenkins@sativus.space',
        subject: "ERROR CI: Project name -> ${env.JOB_NAME}", to: "gaborka812@gmail.com", attachLog: true;
    }
  }
}
