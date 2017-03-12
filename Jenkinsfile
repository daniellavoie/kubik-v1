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
        docker.build("daniellavoie/kubik", "kubik/kubik-server")

        withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'github-daniellavoie', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
	      sh "docker login --password=${PASSWORD} --username=${USERNAME}"

          sh "docker tag ${USERNAME}/kubik ${USERNAME}/kubik:" + env.BRANCH_NAME
          sh "docker push ${USERNAME}/kubik:${env.BRANCH_NAME}"

          if(env.BRANCH_NAME == "master")
		    sh "docker tag ${USERNAME}/kubik ${USERNAME}/kubik:latest"
            sh "docker push ${USERNAME}/kubik:latest"
        }
        
        docker.build("daniellavoie/kubik-product-vehicule", "kubik-product/kubik-product-vehicule/kubik-product-vehicule-server")

        withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'github-daniellavoie', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
	      sh "docker login --password=${PASSWORD} --username=${USERNAME}"

          sh "docker tag ${USERNAME}/kubik-product-vehicule ${USERNAME}/kubik-product-vehicule:" + env.BRANCH_NAME
          sh "docker push ${USERNAME}/kubik-product-vehicule:${env.BRANCH_NAME}"

          if(env.BRANCH_NAME == "master")
		    sh "docker tag ${USERNAME}/kubik-product-vehicule ${USERNAME}/kubik:latest"
            sh "docker push ${USERNAME}/kubik-product-vehicule:latest"
        }
      }
    }
  }

  stage('Test') {

  }
}