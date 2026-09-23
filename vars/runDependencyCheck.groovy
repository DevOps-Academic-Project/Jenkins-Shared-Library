def call(Map config = [:]) {
    def tool = config.tool ?: 'DP-Check'
    dependencyCheck additionalArguments: '--format XML', odcInstallation: tool
    dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
}
