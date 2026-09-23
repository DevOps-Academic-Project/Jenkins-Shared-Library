def call(Map config) {
    def imageName = config.imageName
    def dockerfilePath = config.dockerfilePath ?: 'Dockerfile'
    def credentialsId = config.credentialsId ?: 'a49f0deb-93ab-4574-954f-3927d9f76b39'
    def tag = config.tag ?: env.BUILD_NUMBER

    env.PATH = "/opt/homebrew/bin:${env.PATH}"

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
