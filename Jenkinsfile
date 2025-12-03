pipeline {
    agent any

    triggers {
        // Revisa cambios en GitHub cada minuto
        pollSCM '* * * * *' 
    }

    environment {
        DOCKER_IMAGE = 'matydocker1/backend:latest'
        DOCKER_CREDS = 'docker-hub-credentials'
        // 🚨 TRUCO DE MAGIA: Desactivar BuildKit para evitar problemas de red
        DOCKER_BUILDKIT = '0'
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
                    // Aseguramos que el wrapper tenga permisos de ejecución
                    sh "chmod +x mvnw"
                    sh "./mvnw test"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // 1. Forzamos la descarga de la imagen base PRIMERO
                    echo "--> Descargando imagen base eclipse-temurin..."
                    sh "docker pull eclipse-temurin:21-jdk"

                    // 2. Construimos usando el modo Legacy
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
                        // Inyectamos las credenciales de la BD para que docker-compose las vea
                        withCredentials([
                            string(credentialsId: 'db-url', variable: 'DB_URL'),
                            usernamePassword(credentialsId: 'db-creds', usernameVariable: 'DB_USERNAME', passwordVariable: 'DB_PASSWORD')
                        ]) {
                            // Aseguramos usar la imagen recién construida
                            sh "docker compose up -d --no-deps --force-recreate backend1 backend2 backend3"
                        }
                    }
                    sh "docker image prune -f"
                }
            }
        }
    }
}
