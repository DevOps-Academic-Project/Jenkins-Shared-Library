def call(Map config = [:]) {
    def tool = config.tool ?: 'DP-Check'
    def nvdApiKeyCredentialsId = config.nvdApiKeyCredentialsId ?: 'nvd-api-key'

    withCredentials([string(credentialsId: nvdApiKeyCredentialsId, variable: 'NVD_API_KEY')]) {
        dependencyCheck additionalArguments: "--format XML --nvdApiKey ${NVD_API_KEY}", odcInstallation: tool
    }
    dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
}
