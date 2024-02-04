import java.text.SimpleDateFormat
def startDate
def endDate
def projectId
def moduleId
def projectdetails
def applicationType
def branch = scm.branches[0].name.replaceAll("[^a-zA-Z0-9 ]+", "")
def moduleName = scm.projectName
def projectName = scm.repositoryName
def newBuildId = "${branch}.0.0.${BUILD_ID}"
def autodeploy
def springboottestcommand
def nexusip
def domainip
def artifact
def environment
def noservergroup
def jbossenv
def mvncommand
def user
def propsemail
def emailid
def autotestspring
def buildfailbody
def buildfailsubject
def deployfailbody
def deployfailsubject
def testfailbody
def testfailsubject
def jbossdomain
def jboss = "${branch}"
def jbossIP
def devops
def techm
def sonarenable
def sonarreporturl
def sonarenablelist
def sonarstatusproject
def sonarResult
def sonarfailbody
def dependencyreport
def dependencyrepourl
def sonarkeyprefix
def sonarkey
def jbosscredid
def commitid
def unwantedfiles = " "
def tmpfile
def codetype
def props
def app
def assets
def e2e
def values
def scannerHome
def dependencysubject
def sonarscansuccess
def moduleleademail
def deploy
def disable
def undeploy
def autotestangular
def enableadopt
def ipcheck
def jndi
def ipchecklist
def ipcheckincode
def iplist = ""
def ipharcodesubject = ""
def ipharcodebody = ""
def checklist = []
def enablejndi
def binaryenable
def uiprofile
pipeline {
  agent any
  stages {
    stage('CODE') {
      steps {
        script {
          def repourl = sh(script: 'git remote -v', returnStdout: true)
          def repourl1 = repourl.split("	")[1]
          def repo = repourl1.split(" ")[0]
          def CODE = readJSON text: "{'url':'${repo}','scm':'bitbucket','branch':'${branch}','credId':'idbit'}"
          git url: CODE.url, credentialsId: CODE.credId, branch: CODE.branch
        }
      }
    }
    stage('Build') {
      steps {
        script {
          dir("/app/") {
            propsemail = readProperties file: '/app/emailds.properties'
            emailid = propsemail.email
            moduleleademail = propsemail.
            "${moduleName}lead"
            devops = propsemail.devops
            techm = propsemail.techm
            testfailmessage = propsemail.testfailmessage
            testfailsubject = propsemail.testfailsubject
            sonarfailmessage = propsemail.sonarfailmessage
            dependencyreport = propsemail.dependencyreport
            dependencyrepourl = propsemail.dependencyrepourl
            nexusip = propsemail.nexusip
            buildfailbody = propsemail.buildfailbody
            buildfailsubject = propsemail.buildfailsubject
            deployfailbody = propsemail.deployfailbody
            sonarscansuccess = propsemail.sonarscansuccess
            testfailbody = propsemail.testfailbody
            dependencysubject = propsemail.dependencysubject
            deployfailsubject = propsemail.deployfailsubject
            jbossdomain = propsemail.jbossdomain
            enableadopt = propsemail.enableadopt
            enablejndi = propsemail.enablejndi
            binaryenable = propsemail.binaryenable
            jndi = propsemail.jndi
            jboss = "${jboss}".toUpperCase()
            jbossenv = "${jbossdomain}_${jboss}"
            jbossIP = propsemail.
            "${branch}jboss"
            jbosscredid = propsemail.
            "${branch}jbosscredid"
            sonarenable = propsemail.sonarenable
            sonarreporturl = propsemail.sonarreporturl
            sonarkeyprefix = propsemail.sonarkeyprefix
            sonarreporturl = "${sonarreporturl}${sonarkey}"
            ipcheck = propsemail.ipcheck
            autodeploy = propsemail.autodeploy
            uiprofile = propsemail.uiprofile
            noservergroup = propsemail.noservergroup
            autotestangular = propsemail.autotestangular
            autotestspring = propsemail.autotestspring
            domainip = propsemail.domainip
            branch = "${branch}".toLowerCase()
            jbossenv = propsemail.
            "jbossenv${branch}"
            springboottestcommand = propsemail.springboottestcommand
            projectdetails = propsemail.
            "${moduleName}-${projectName}"
            moduleId = projectdetails.split(",")[0]
            projectId = projectdetails.split(",")[1]
          }
          if (fileExists("${env.WORKSPACE}/pom.xml")) {
            applicationType = "springboot"
          } else {
            applicationType = "angular"
          }
          user = sh returnStdout: true, script: "git log -1 --pretty=format:'%ae' "
          user = user.split("@")[0] + "${techm}"
          commitid = sh returnStdout: true, script: "git log --format='%H' -n 1 "
          commitmessage = sh returnStdout: true, script: "git log --format=%B -n 1 ${commitid} "
          print "${commitid}  ::::${commitmessage}"
          currentBuild.displayName = "${projectName}_${newBuildId}"
          artifact = "${nexusip}/${moduleName}/${projectName}/${newBuildId}/"
          if (!applicationType.contains("spring")) {
            props = readJSON file: "${env.WORKSPACE}/package.json"
            warname = props.name
            archive = "${warname}.war"
            ipchecklist = ipcheck.split(',').collect {
              it as String
            };
            ipchecklist.each {
              item ->
                dir("${env.WORKSPACE}/src/app") {
                  app = sh(script: "grep -rnw '${env.WORKSPACE}/src/app' -e '${item}'   ", returnStatus: true) == 0
                }
              dir("${env.WORKSPACE}/src/assets") {
                assets = sh(script: "grep -rnw '${env.WORKSPACE}/src/assets' -e '${item}'   ", returnStatus: true) == 0
              }
              dir("${env.WORKSPACE}/e2e") {
                e2e = sh(script: "grep -rnw '${env.WORKSPACE}/e2e' -e '${item}'   ", returnStatus: true) == 0
              }
              if ("${app}" == "true" || "${assets}" == "true" || "${e2e}" == "true") {
                iplist = "${iplist}${item}"
                checklist.add("${item}")
              }
            }
            if (checklist.size() > 0) {
              print "List of ${checklist} are hard coded  in sourcecode Please check and do the requred changes"
              if ("${branch}".contains("prod")) {
                return failure
              }
            } else {
              print "There is no hardcoded details in code "
            }
            if (fileExists("${env.WORKSPACE}/src/environments/environment.${branch}.ts")) {
              sh "npm install && ng build --configuration=${branch}"
              dir("${env.WORKSPACE}/dist/${warname}") {
                sh "jar cvf ${archive} ."
              }
            } else if (!"${uiprofile}".contains("${branch}")) {
              sh "npm install && ng build"
              dir("${env.WORKSPACE}/dist/${warname}") {
                sh "jar cvf ${archive} ."
                print "There is no angular profile please create angular profile and commit ..... "
              }
            } else if ("${uiprofile}".contains("${branch}")) {
              print "There is no angular profile please create angular profile and commit ..... "
              return failure
            }
          } else { //springboot
            ipchecklist = ipcheck.split(',').collect {
              it as String
            };
            ipchecklist.each {
              item ->
                print "ip check list iteration:: ${item}"
              if ("${item}".contains("hikari")) {
                dir("${env.WORKSPACE}/src/main/resources/") {
                  ipcheckincode = sh(script: "grep -rnw '${env.WORKSPACE}/src/main/resources/' -e 'hikari'   ", returnStatus: true) == 0
                }
              } else if ("${item}" == "jdbc:oracle:thin") {
                dir("${env.WORKSPACE}/src/main/resources/") {
                  def sit = sh(script: "grep -rnw '${env.WORKSPACE}/src/main/resources/application-sit.properties' -e '${item}'   ", returnStatus: true) == 0
                  def uat = sh(script: "grep -rnw '${env.WORKSPACE}/src/main/resources/application-uat.properties' -e '${item}'   ", returnStatus: true) == 0
                  def qa = sh(script: "grep -rnw '${env.WORKSPACE}/src/main/resources/application-qa.properties' -e '${item}'   ", returnStatus: true) == 0
                  def prod = sh(script: "grep -rnw '${env.WORKSPACE}/src/main/resources/application-prod.properties' -e '${item}'   ", returnStatus: true) == 0
                  def dr = sh(script: "grep -rnw '${env.WORKSPACE}/src/main/resources/application-dr.properties' -e '${item}'   ", returnStatus: true) == 0
                  if (("${jndi}".contains("sit") && "${sit}" == "true") || ("${jndi}".contains("uat") && "${uat}" == "true") || ("${jndi}".contains("qa") && "${qa}" == "true") || ("${jndi}".contains("prod") && "${prod}" == "true") || ("${jndi}".contains("dr") && "${dr}" == "true")) {
                    print "Please USE Jndi and Commit code in all application properties "
                    ipcheckincode = "true"
                    if ("${enablejndi}" == "true")
                      return failure
                  }
                }
              } else {
                dir("${env.WORKSPACE}/src") {
                  ipcheckincode = sh(script: "grep -rnw '${env.WORKSPACE}' -e '${item}'   ", returnStatus: true) == 0
                }
              }
              print " current checking string is :: ${item} :: ${ipcheckincode} Checking ip in code base is : ${item}"
              if (ipcheckincode) {
                iplist = "${iplist}${item}"
                print "${iplist}"
                print "In Code base ${item} is present"
                checklist.add("${item}")
                if ("${item}".contains("hikari")) {
                  print "Hikari?? is present "
                  sh "mvn clean"
                  dir("${env.WORKSPACE}/src/main/resources/") {
                    sh " sed -i '/hikari/d' *.properties"
                  }
                }
              }
            }
            if (checklist.size() > 0) {
              print "List of ${checklist} are hard coded  in sourcecode Please check and do the requred changes"
              if ("${branch}".contains("prod")) {
                return failure
              }
            }
            if (dependencyreport.contains("${branch}")) {

              dir("/app/log4j/${projectName}") {
                fileOperations([folderCreateOperation("${branch}")])
                dir("${env.WORKSPACE}/") {
                  sh "${mvncommandinstall}"
                } //install
                dir("${env.WORKSPACE}/target/") {
                  sh "cp dependency-check-report.html /app/log4j/${projectName}/${branch}/dependency-check-report-${newBuildId}.html "
                  dependencyrepourl = dependencyrepourl + "${projectName}/${branch}/dependency-check-report-${newBuildId}.html"
                } //move dependency
              } //dir

            }
            dir("${env.WORKSPACE}/") {
              pom = readMavenPom file: 'pom.xml'
              values = "${pom}".split(':')
              warname = "${values[1]}.war"
              print "${projectName} application warname  is :::::${warname}"

              mvncommand = "mvn clean package -P${branch} -DskipTests "
              sh "${mvncommand}"
            }
          } //spring boot 

        }
      }
      post {
        success {
          script {
            endDate = new Date()
            if ("${enableadopt}" == "true") {
              adoptBuildFeedback buildDisplayName: "${projectName}_${newBuildId}.", buildEndedAt: "${endDate}", buildStartedAt: "${startDate}", buildUrl: "${BUILD_URL}", projectId: projectId, status: "SUCCESS"
            }
          }
        }
        failure {
          script {
            endDate = new Date()
            cleanWs()
            if ("${enableadopt}" == "true") {
              adoptBuildFeedback buildDisplayName: "${projectName}_${newBuildId}.", buildEndedAt: "${endDate}", buildStartedAt: "${startDate}", buildUrl: "${BUILD_URL}", projectId: projectId, status: "FAILURE"
              mail bcc: '', body: "<b>${moduleName} ${buildfailbody} </b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> URL de build: ${env.BUILD_URL}", cc: "${moduleleademail}", charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "${buildfailsubject} ${env.JOB_NAME}", to: "${user}";
            }
          }
        }
      }
    }

    stage('Unit Test') {
      steps {
        script {
          if (applicationType.contains("spring") && "${autotestspring}".contains("${branch}")) {
            sh "${springboottestcommand}"
            print "Springboot Test cases run successfully"
          } else if (!applicationType.contains("spring") && "${autotestangular}".contains("${branch}")) {
            print "Angular code Test cases run successfully"
          }
        }
      }
    }

    
    stage('FOSS/OSS Scan') {
      steps {
        script {
          print "==============FOSS/OSS Scan==========="
          if (applicationType.contains("spring")) {
            print "==============FOSS/OSS Scan===========${dependencyrepourl}"
            // mail bcc: '', body: "<b>${moduleName} -FOSS/OSS Scan Report </b><br>Project: ${env.JOB_NAME} <br>FOSS/OSS Scan Report URL : ${dependencyrepourl}", cc: "${moduleleademail}", charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: " ${dependencysubject}", to: "${user}";
            print "==============email sent FOSS/OSS Scan===========${dependencyrepourl}"
          }
        }

      }
    }

    stage('Binary Management') {
      steps {
        script {
          if ("${autodeploy}".contains("${branch}")) {
            if ("${binaryenable}" == "true") {
              withCredentials([usernamePassword(credentialsId: 'nexusid', passwordVariable: 'password', usernameVariable: 'username')]) {
                if (applicationType.contains("spring")) {
                  executeCmd("curl -v -u  $username:$password   --upload-file 'target/${warname}'  '${artifact}'");
                  executeCmd("curl -v -u  $username:$password   --upload-file 'pom.xml'  '${artifact}'");
                } else {
                  executeCmd("curl -v -u  $username:$password   --upload-file '${env.WORKSPACE}/dist/${warname}/${archive}'  '${artifact}'");
                  executeCmd("curl -v -u  $username:$password   --upload-file 'package.json'  '${artifact}'");
                }
              }
            }
          }
        }
      }
    } //stage

    stage("Deployment") {
      steps {
        script {
          if ("${autodeploy}".contains("${branch}")) {
            withCredentials([usernamePassword(credentialsId: "${jbosscredid}", passwordVariable: "password", usernameVariable: 'username')]) {
              dir('/app/jboss-eap-7.3/bin/') {
                if (applicationType.contains("spring")) {
                  if ("${noservergroup}".contains("${branch}")) {
                    disable = sh(script: " ./jboss-cli.sh --timeout=30000 --connect --controller=${jbossIP}:9990 --user=$username  --password=$password   --command='deployment disable ${warname} ' ", returnStatus: true) == 0
                  } else {
                    disable = sh(script: " ./jboss-cli.sh --timeout=30000 --connect --controller=${jbossIP}:9990 --user=$username  --password=$password   --command='deployment disable ${warname} --server-groups=${jbossenv} ' ", returnStatus: true) == 0
                  }
                } else if (applicationType.contains("angular")) {
                  if ("${noservergroup}".contains("${branch}")) {
                    disable = sh(script: " ./jboss-cli.sh --timeout=30000 --connect --controller=${jbossIP}:9990 --user=$username  --password=$password   --command='deployment disable ${archive} ' ", returnStatus: true) == 0
                  } else {
                    disable = sh(script: " ./jboss-cli.sh --timeout=30000 --connect --controller=${jbossIP}:9990 --user=$username  --password=$password   --command='deployment disable ${archive}  --server-groups=${jbossenv} ' ", returnStatus: true) == 0
                  }
                }
                if (disable == true) {
                  print "Disabled successfully"
                  if (applicationType.contains("spring")) {
                    if ("${noservergroup}".contains("${branch}")) {
                      undeploy = sh(script: " ./jboss-cli.sh --timeout=30000 --connect --controller=${jbossIP}:9990 --user=$username  --password=$password   --command='undeploy ${warname} ' ", returnStatus: true) == 0
                    } else {
                      undeploy = sh(script: " ./jboss-cli.sh --timeout=30000 --connect --controller=${jbossIP}:9990 --user=$username  --password=$password   --command='undeploy ${warname} --server-groups=${jbossenv}  ' ", returnStatus: true) == 0
                    }
                  } else if (applicationType.contains("angular")) {
                    if ("${noservergroup}".contains("${branch}")) {
                      undeploy = sh(script: " ./jboss-cli.sh --timeout=30000 --connect --controller=${jbossIP}:9990 --user=$username  --password=$password   --command='undeploy ${archive} ' ", returnStatus: true) == 0
                    } else {
                      undeploy = sh(script: " ./jboss-cli.sh --timeout=30000 --connect --controller=${jbossIP}:9990 --user=$username  --password=$password   --command='undeploy ${archive} --server-groups=${jbossenv}  ' ", returnStatus: true) == 0
                    }
                  }
                  if (undeploy == true) {
                    print "Undeployed  successfully ${undeploy}"
                    if (applicationType.contains("spring")) {
                      if ("${noservergroup}".contains("${branch}")) {
                        deploy = sh(script: "./jboss-cli.sh --timeout=30000 --connect --controller=${jbossIP}:9990 --user=$username  --password=$password     --command='deploy ${env.WORKSPACE}/target/${warname}  ' ", returnStatus: true) == 0
                      } else {
                        deploy = sh(script: "./jboss-cli.sh --timeout=30000 --connect --controller=${jbossIP}:9990 --user=$username  --password=$password     --command='deploy ${env.WORKSPACE}/target/${warname} --server-groups=${jbossenv} ' ", returnStatus: true) == 0
                      }
                    } else if (applicationType.contains("angular")) {
                      if ("${noservergroup}".contains("${branch}")) {
                        deploy = sh(script: "./jboss-cli.sh --timeout=30000 --connect --controller=${jbossIP}:9990 --user=$username  --password=$password     --command='deploy ${env.WORKSPACE}/dist/${warname}/${archive} ' ", returnStatus: true) == 0
                      } else {
                        deploy = sh(script: "./jboss-cli.sh --timeout=30000 --connect --controller=${jbossIP}:9990 --user=$username  --password=$password     --command='deploy ${env.WORKSPACE}/dist/${warname}/${archive} --server-groups=${jbossenv}  ' ", returnStatus: true) == 0
                      }
                    }
                    if (deploy == false) {
                      print "Application unable to Deploy"
                      if ("${enableadopt}" == "true") {
                        mail bcc: '', body: "<b>${moduleName}  ${deployfailbody} </b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> URL de build: ${env.BUILD_URL}", cc: "${moduleleademail}", charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "${deployfailsubject}  ${env.JOB_NAME}", to: "${user}";
                      }
                      withCredentials([usernamePassword(credentialsId: "REDEPLOY", passwordVariable: "password", usernameVariable: 'username')]) {
                        def output = sh returnStdout: true, script: "curl --user '$username':'$password' '${propsemail}/job/${env.JOB_NAME}/lastSuccessfulBuild/buildNumber'"
                        print "deploy failure for war file issue :: downloding backup one::" + output;
                      }
                      dir("${env.WORKSPACE}/") {

                        def artifactnew = "${nexusip}/${moduleName}/${projectName}/${buildids}.${output}/${warname}";
                        print " artifact ::" + artifactnew;
                        def newbuilddd = sh(script: " wget  --user='developer' and --password='java123#'   -X GET '${artifactnew}'", returnStatus: true) == 0
                        withCredentials([usernamePassword(credentialsId: "${jbosscredid}", passwordVariable: 'password', usernameVariable: 'username')]) {
                          dir('/app/jboss-eap-7.3/bin/') {
                            def deploynews22 = sh(script: "./jboss-cli.sh --timeout=30000 --connect --controller=${jbossIP}:9990  --user=$username  --password=$password     --command='deploy ${env.WORKSPACE}/${warname}   ' ", returnStatus: true) == 0
                            print "rollback :::: deployed ::" + deploynews22
                            return failure
                          }
                        } //nexus
                      }
                    } //deploy
                    else {
                      print "Deployed application Successfully"
                      return true
                    }
                  } //undeploy
                  else {
                    print "undeploy failed"
                    deploy = sh(script: "./jboss-cli.sh --timeout=30000 --connect --controller=${jbossIP}:9990 --user=$username  --password=$password     --command='deploy ${env.WORKSPACE}/dist/${warname}/${archive}  ' ", returnStatus: true) == 0
                    if ("${enableadopt}" == "true") {
                      mail bcc: '', body: "<b>${moduleName}  UN-${deployfailbody} </b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> URL de build: ${env.BUILD_URL}", cc: "${moduleleademail}", charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "${deployfailsubject}  ${env.JOB_NAME}", to: "${user}";

                    }
                    return failure
                  } //undeploy else

                } //disable
                else {
                  print "Disabled  failed Deploying application"
                  if (applicationType.contains("spring")) {
                    if ("${noservergroup}".contains("${branch}")) {
                      deploy = sh(script: "./jboss-cli.sh --timeout=30000 --connect --controller=${jbossIP}:9990 --user=$username  --password=$password     --command='deploy ${env.WORKSPACE}/target/${warname}  ' ", returnStatus: true) == 0
                    } else {
                      deploy = sh(script: "./jboss-cli.sh --timeout=30000 --connect --controller=${jbossIP}:9990 --user=$username  --password=$password     --command='deploy ${env.WORKSPACE}/target/${warname} --server-groups=${jbossenv} ' ", returnStatus: true) == 0

                    }
                  } else if (applicationType.contains("angular")) {
                    if ("${noservergroup}".contains("${branch}")) {
                      deploy = sh(script: "./jboss-cli.sh --timeout=30000 --connect --controller=${jbossIP}:9990 --user=$username  --password=$password     --command='deploy ${env.WORKSPACE}/dist/${warname}/${archive} ' ", returnStatus: true) == 0
                    } else {
                      deploy = sh(script: "./jboss-cli.sh --timeout=30000 --connect --controller=${jbossIP}:9990 --user=$username  --password=$password     --command='deploy ${env.WORKSPACE}/dist/${warname}/${archive}  --server-groups=${jbossenv} ' ", returnStatus: true) == 0

                    }
                  }

                  if (deploy == false) {
                    print "Application unable to Deploy"
                    if ("${enableadopt}" == "true") {
                      print "DEPLOY FAILURE  ::" + user;
                      mail bcc: '', body: "<b>${moduleName}  ${deployfailbody} </b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> URL de build: ${env.BUILD_URL}", cc: "${moduleleademail}", charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "${deployfailsubject}  ${env.JOB_NAME}", to: "${user}";
                      print "email sent to   ::" + user;
                    }
                    return failure
                  } else if (deploy == true) {
                    print "Deployed application Successfully"
                  }
                }
              } //dir  

            } //jboss
          }
        } //script
      } //steps

      post {
        failure {
          script {
            try {

            } finally {
              script {
                cleanWs()
                currentBuild.result = 'SUCCESS'
              }
            }
          }
        }
      } //post
    } //stage

    stage('WEB Inspect') {
      steps {
        script {
          print "=============WEB Inspect==========="
        }

      }
    }

    stage('CLEAN WS') {
      steps {
        script {
          cleanWs()
          currentBuild.result = 'SUCCESS'
          currentBuild.result = "SUCCESS"
        }
      }
    }

  } //stages
} //pipeline

//Helper Methods

void executeCmd(String CMD) {
  if (isUnix()) {
    sh "echo linux"
    sh CMD
  } else {
    bat "echo windows"
    bat CMD
  }
}
