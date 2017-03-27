node {
  stage ("Build") {
    withMaven(
      maven: 'maven-3',
      globalMavenSettingsConfig: 'kubik-settings',
      mavenLocalRepo: '.repository'
    ) {
      checkout scm

      // Run the maven build
      sh "mvn clean deploy"

      docker.withServer("unix:///var/run/docker.sock") {
        docker.build("daniellavoie/kubik", "kubik")

        withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'github-daniellavoie', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
	      sh "docker login --password=${PASSWORD} --username=${USERNAME}"

          sh "docker tag ${USERNAME}/kubik ${USERNAME}/kubik:" + env.BRANCH_NAME
          //sh "docker push ${USERNAME}/kubik:${env.BRANCH_NAME}"

          if(env.BRANCH_NAME == "master")
		    sh "docker tag ${USERNAME}/kubik ${USERNAME}/kubik:latest"
            //sh "docker push ${USERNAME}/kubik:latest"
        }
        
      }
    }
  }

  stage('Test') {

  }
}