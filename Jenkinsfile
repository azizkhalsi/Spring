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
}