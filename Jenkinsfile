pipeline {
    agent any

    environment {
        IMAGE_NAME = "hadil-app"
        DOCKER_HUB_REPO = "hamamou99/${IMAGE_NAME}"
    }

    stages {
        stage('Build Docker Image') {
            steps {
                script {
                    sh "docker build -t ${IMAGE_NAME} ."
                }
            }
        }

        stage('Tag Docker Image') {
            steps {
                script {
                    sh "docker tag ${IMAGE_NAME} ${DOCKER_HUB_REPO}:latest"
                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_HUB_USERNAME', passwordVariable: 'DOCKER_HUB_PASSWORD')]) {
                        sh '''
                        echo "$DOCKER_HUB_PASSWORD" | docker login -u "$DOCKER_HUB_USERNAME" --password-stdin
                        docker push ${DOCKER_HUB_REPO}:latest
                        '''
                    }
                }
            }
        }

        stage('Analyze Code with SonarQube') {
            steps {
                script {
                    withSonarQubeEnv('sonarserver') {
                        withCredentials([string(credentialsId: 'sonartoken', variable: 'SONAR_TOKEN')]) {
                            sh '''
                                mvn sonar:sonar \
                                    -Dsonar.projectKey=springproject \
                                    -Dsonar.host.url=http://192.168.33.10:9000 \
                                    -Dsonar.login=${SONAR_TOKEN} \
                                    -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                            '''
                        }
                    }
                }
            }
        }

        stage('Quality Gate') {
            steps {
                script {
                    def qg = waitForQualityGate()
                    if (qg.status != 'OK') {
                        error "Quality Gate failed: ${qg.status}"
                    } else {
                        echo "Quality Gate passed: ${qg.status}"
                    }
                }
            }
        }
    }

    post {
        success {
            echo "Docker image pushed successfully to Docker Hub."
        }
        failure {
            echo "Pipeline failed. Please check the logs."
        }
        cleanup {
            sh "docker system prune -f"
        }
    }
}
