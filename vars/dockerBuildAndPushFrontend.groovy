def call(Map config) {
    def imageName = config.imageName
    def dockerfilePath = config.dockerfilePath ?: 'docker/Dockerfile'
    def credentialsId = config.credentialsId ?: 'docker-hub-creds'
    def tag = config.tag ?: env.BUILD_NUMBER

    stage('Build Docker Image') {
        sh "docker build -f ${dockerfilePath} -t ${imageName}:${tag} ."
    }

    stage('Push Docker Image') {
        withCredentials([usernamePassword(
            credentialsId: credentialsId,
            usernameVariable: 'DOCKER_USER',
            passwordVariable: 'DOCKER_PASS'
        )]) {
            sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
            sh "docker push ${imageName}:${tag}"
        }
    }
}
