pipeline {
    agent any

    triggers {
        // Revisa cambios en GitHub cada minuto
        pollSCM '* * * * *' 
    }

    environment {
        DOCKER_IMAGE = 'matydocker1/backend:latest'
        DOCKER_CREDS = 'docker-hub-credentials'
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/Maty-git/BackendTingeso.git'
            }
        }

        stage('Test') {
            steps {
                script {
                    echo "--> Ejecutando pruebas unitarias..."
                    
                    sh "chmod +x mvnw"
                    
                    sh "./mvnw clean test"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    
                    echo "--> Descargando imagen base eclipse-temurin..."
                    sh "docker pull eclipse-temurin:21-jdk"

                    
                    echo "--> Construyendo imagen..."
                    sh "docker build --no-cache -t ${DOCKER_IMAGE} ."
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: DOCKER_CREDS, usernameVariable: 'USER', passwordVariable: 'PASS')]) {
                        sh "docker login -u $USER -p $PASS"
                        sh "docker push ${DOCKER_IMAGE}"
                    }
                }
            }
        }

        stage('Deploy Local (Update Services)') {
            steps {
                script {
                    dir('deploy') { 
                        
                        withCredentials([
                            string(credentialsId: 'db-url', variable: 'DB_URL'),
                            usernamePassword(credentialsId: 'db-creds', usernameVariable: 'DB_USERNAME', passwordVariable: 'DB_PASSWORD')
                        ]) {
                            
                            sh "docker compose up -d --no-deps --force-recreate backend1 backend2 backend3"
                        }
                    }
                    sh "docker image prune -f"
                }
            }
        }
    }
}
