def call(Map config = [:]) {
    def buildConfig = config.buildConfig ?: 'production'

    stage('Install dependencies') {
        sh 'npm ci'
    }


    stage('Test') {
        sh "npm test -- --watch=false"
    }

    stage('Build') {
        sh "npm run build -- --configuration ${buildConfig}"
    }
}
