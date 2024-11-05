pipeline {
    agent any
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
    }

     // Stage 2: Run Docker Compose for Spring Project
            stage('Docker Compose Spring Project') {
                steps {
                    sh 'docker-compose -f docker-compose.yml up -d'
                }
            }

            // Stage 3: Run Docker Compose for Tools
            stage('Docker Compose Tools') {
                steps {
                    sh 'docker-compose -f docker-composetools.yml up -d'
                }
            }

            stage('Build with Maven') {
                steps {
                    sh 'mvn clean compile jacoco:report'
                }
            }

            stage('Test Unitaires et Jacoco') {
                steps {
                    sh 'mvn clean test'
                }
            }
            stage('Build') {
                steps {
                    sh 'mvn package'
                }
            }

    // Stage 5: Analyze Code with SonarQube
            stage('MVN SonarQube') {
                steps {
                    script {
                        withSonarQubeEnv('sonarserver') {
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