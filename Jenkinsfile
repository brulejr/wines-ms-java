pipeline {
    agent {
        docker {
            image 'gradle:7-jdk11'
            reuseNode true
        }
    }
    environment {
        DOCKER_IMAGE = "wines-ms-java"
        DOCKERHUB_CREDENTIALS = credentials('dockerhub')
        SSH = "#ssh -o StrictHostKeyChecking=no -l ${DOCKER_DEPLOY_USER} ${DOCKER_DEPLOY_HOST}"
    }
    stages {
        stage ('Checkout') {
            steps {
                checkout scm
                sh 'ls -lat'
            }
        }
        stage("Build") {
            steps {
                sh 'gradle clean build'
                junit "**/build/test-results/test/*.xml"
                jacoco(
                    execPattern: 'build/jacoco/jacoco.exec'
                )
            }
        }
        stage('Publish') {
            steps {
                sh "gradle jib"
            }
        }
        stage("Deploy") {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub’,
                        usernameVariable: 'DOCKER_HUB_USER’,
                        passwordVariable: 'DOCKER_HUB_PASSWD'
                        )
                ]) {
                    sshagent(credentials: ['jenkins_deploy']) {
                        sh "${SSH} docker pull ${DOCKER_HUB_USER}/${DOCKER_IMAGE}:latest"
                        sh "${SSH} docker stop ${DOCKER_IMAGE}"
                        sh "${SSH} docker rm ${DOCKER_IMAGE}"
                        sh "${SSH} docker run -p 4050:4050 -d --name ${DOCKER_IMAGE} ${DOCKER_HUB_USER}/${DOCKER_IMAGE}:latest"
                    }
                }
            }
        }
    }
}
