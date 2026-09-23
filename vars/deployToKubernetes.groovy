def call(Map config) {
    def manifestsPath = config.manifestsPath ?: 'k8s'
    def deploymentFile = config.deploymentFile
    def imageName = config.imageName
    def tag = config.tag ?: env.BUILD_NUMBER

    sh "sed -i '' 's|image:.*|image: ${imageName}:${tag}|' ${deploymentFile}"
    sh "kubectl apply -f ${manifestsPath}/"
}
