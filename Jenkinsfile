pipeline {
    agent any
    tools {
            maven 'M2_HOME'       // Ensure "M2_HOME" is configured in Jenkins
            jdk 'JAVA_HOME'       // Ensure "JAVA_HOME" is configured in Jenkins
        }
    triggers {
        // Poll GitHub for changes, or use GitHub webhook
        githubPush()
    }
    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'raefkhalifa', credentialsId: 'githubcred', url: 'https://github.com/azizkhalsi/Spring.git'
            }
        }

         // Stage 5: Analyze Code with SonarQube
                    stage('MVN SonarQube') {
                        steps {
                            script {
                                withSonarQubeEnv('sq1') {
                                    withCredentials([string(credentialsId: 'jenkins-sonar', variable: 'SONAR_TOKEN')]) {
                                        sh '''
                                            mvn sonar:sonar \
                                                -Dsonar.projectKey=springproject \
                                                -Dsonar.host.url=http://10.0.2.15:9000 \
                                                -Dsonar.login=${SONAR_TOKEN} \
                                                -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                                        '''
                                    }
                                   }
                                }
                            }
                        }
    }





}