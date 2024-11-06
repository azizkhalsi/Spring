pipeline {
    agent any

    environment {
        IMAGE_NAME = "hadil-app"
        DOCKER_HUB_REPO = "hamamou99/${IMAGE_NAME}"
    }

   
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_HUB_USERNAME', passwordVariable: 'DOCKER_HUB_PASSWORD')]) {
                       
pipeline {
    agent any
    triggers {
        // Poll GitHub for changes, or use GitHub webhook
        githubPush()
    }
    stages {
 
        stage('Checkout Code') {
            steps {
                git branch: 'hadil-amamou', credentialsId: 'github-creds', url: 'https://github.com/azizkhalsi/Spring.git'
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
 
        // Stage for Quality Gate
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
 
        // Stage 3: Deploy to Nexus
        stage('Deploy to Nexus') {
            steps {
                script {
                    nexusArtifactUploader(
                        nexusVersion: 'nexus3',
                        protocol: 'http',
                        nexusUrl: '192.168.33.10:8081',
                        groupId: 'com.projet',
                        version: '0.0.1',
                        repository: 'springproject',
                        credentialsId: 'NEXUS_CRED',
                        artifacts: [
                            [
                                artifactId: 'tpAchatProject',
                                classifier: '',
                                file: 'target/tpAchatBuild.jar',
                                type: 'jar'
                            ]
                        ]
                    )
                }
            }
            post {
                success {
                    echo 'Deployment to Nexus successful.'
                }
                failure {
                    echo 'Error during Nexus deployment.'
                }
            }
        }
    }
 
 
}
