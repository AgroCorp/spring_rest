pipeline {
    agent any

    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven "M3"
    }

    stages {
        stage('Build') {
            steps {
                // Get some code from a GitHub repository
                git 'https://github.com/AgroCorp/spring_rest.git'

                // Run Maven on a Unix agent.
                sh "mvn -B --file pom.xml -Dmaven.test.skip=true clean package"

                // To run Maven on a Windows agent, use
                // bat "mvn -Dmaven.test.failure.ignore=true clean package"
            }

            post {
                // If Maven was able to run the tests, even if some of the test
                // failed, record the test results and archive the jar file.
                success {
//                    junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.jar'
                }
                failure {
                    mail bcc: '', body: "<b>Error in build</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br>Stage: ${STAGE_NAME} <br> URL de build: ${env.BUILD_URL}", cc: '', charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "ERROR CI: Project name -> ${env.JOB_NAME}", to: "ubuntu@sativus.space";
                }
            }
        }
        stage('SonarQube scan') {
            steps {
                sh "mvn -B --file pom.xml -Dmaven.test.skip=true clean verify sonar:sonar"
            }
        }
    }
}

