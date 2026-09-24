@NonCPS
def getCommitInfo() {
    def changeLogSets = currentBuild.changeSets
    if (changeLogSets.size() > 0) {
        def entries = changeLogSets[0].items
        if (entries.length > 0) {
            def authorName = entry.authorName.toString()
            def msg = entry.msg.toString()
            def commitId = entry.commitId.take(7).toString()
            return "<b>${authorName}</b> — \"${msg}\" (${commitId})"
        }
    }
    return "Aucun changement detecte"
}

def call(Map config = [:]) {
    def status = currentBuild.currentResult
    def statusColor = status == 'SUCCESS' ? '#22c55e' : (status == 'UNSTABLE' ? '#f59e0b' : '#ef4444')
    def statusEmoji = ''
    def recipient = config.recipient ?: 'louayzorai24@gmail.com'
    def sonarProjectKey = config.sonarProjectKey ?: ''
    def dockerImage = config.dockerImage ?: ''
    def grafanaUrl = config.grafanaUrl ?: 'http://grafana.local:30618'

    def commitInfo = getCommitInfo()

    def dockerSection = dockerImage ? """
        <tr><td style="padding:10px 16px;font-weight:600;color:#475569;">Image Docker</td>
        <td style="padding:10px 16px;"><a href="https://hub.docker.com/r/${dockerImage}/tags" style="color:#3b82f6;text-decoration:none;">${dockerImage}:${env.BUILD_NUMBER}</a></td></tr>
    """ : ''

    def sonarSection = sonarProjectKey ? """
        <tr><td style="padding:10px 16px;font-weight:600;color:#475569;">SonarQube</td>
        <td style="padding:10px 16px;"><a href="http://84.247.129.48:9000/dashboard?id=${sonarProjectKey}" style="color:#3b82f6;text-decoration:none;">Voir le rapport d'analyse</a></td></tr>
    """ : ''

    def body = """
    <html>
    <body style="margin:0;padding:0;background:#f1f5f9;font-family:'Segoe UI',Arial,sans-serif;">
        <div style="max-width:600px;margin:24px auto;background:#ffffff;border-radius:12px;overflow:hidden;box-shadow:0 1px 4px rgba(0,0,0,0.08);">
            <div style="background:${statusColor};padding:24px 28px;">
                <h1 style="margin:0;color:#ffffff;font-size:20px;">Build ${status} — ${env.JOB_NAME} #${env.BUILD_NUMBER}</h1>
                <p style="margin:6px 0 0;color:rgba(255,255,255,0.9);font-size:13px;">Duree : ${currentBuild.durationString.replace(' and counting', '')}</p>
            </div>
            <table style="width:100%;border-collapse:collapse;font-size:14px;color:#1e293b;">
                <tr><td style="padding:10px 16px;font-weight:600;color:#475569;">Projet</td><td style="padding:10px 16px;">${env.JOB_NAME}</td></tr>
                <tr style="background:#f8fafc;"><td style="padding:10px 16px;font-weight:600;color:#475569;">Commit</td><td style="padding:10px 16px;">${commitInfo}</td></tr>
                <tr><td style="padding:10px 16px;font-weight:600;color:#475569;">Declencheur</td><td style="padding:10px 16px;">${currentBuild.getBuildCauses()[0]?.shortDescription ?: 'Manuel'}</td></tr>
                ${sonarSection}
                ${dockerSection}
                <tr style="background:#f8fafc;"><td style="padding:10px 16px;font-weight:600;color:#475569;">Monitoring</td>
                <td style="padding:10px 16px;"><a href="${grafanaUrl}" style="color:#3b82f6;text-decoration:none;">Ouvrir Grafana</a></td></tr>
            </table>
            <div style="padding:20px 28px;background:#f8fafc;text-align:center;">
                <a href="${env.BUILD_URL}console" style="display:inline-block;background:#1e293b;color:#ffffff;text-decoration:none;padding:10px 20px;border-radius:8px;font-size:14px;">Voir la console complete</a>
            </div>
            <div style="padding:14px 28px;text-align:center;">
                <p style="margin:0;color:#94a3b8;font-size:11px;">Envoye automatiquement par Jenkins — Projet DevOps ESPRIT ArcTIC</p>
            </div>
        </div>
    </body>
    </html>
    """

    emailext(
        subject: "[${env.JOB_NAME}] Build #${env.BUILD_NUMBER} — ${status}",
        body: body,
        mimeType: 'text/html',
        to: recipient
    )
}
