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
        def DOCKER_REGISTRY_URI = "https://docker.io"
	      
        def kubikImage = docker.build("daniellavoie/kubik", "kubik/kubik-server")
	    
        withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'daniellavoie', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
	      sh "docker login --password=${PASSWORD} --username=${USERNAME} ${DOCKER_REGISTRY_URI}"
	    }  
        
        sh "docker tag daniellavoie/kubik daniellavoie/kubik:" + env.BRANCH_NAME
        
        if(env.BRANCH_NAME == "master")
        
		  sh "docker tag daniellavoie/kubik daniellavoie/kubik:lastest"
        
      }
    }
  }

  stage('Test') {

  }

  stage('Deploy') {

  }
}