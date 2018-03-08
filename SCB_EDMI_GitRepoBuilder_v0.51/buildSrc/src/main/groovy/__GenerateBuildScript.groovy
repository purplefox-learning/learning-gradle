
class GenerateBuildScript {

	String edmiServiceName
	String baseDirectory
	
	GenerateBuildScript(name, baseDir) {
		edmiServiceName = name
		baseDirectory = baseDir
	}
	
	//input
	String gradleScriptTemplate = new File('template.txt').text
	
	//output
	File outputBuildSpec
	
	void run() {
		groovy.text.Template template = new groovy.text.StreamingTemplateEngine().createTemplate(gradleScriptTemplate)
		
		//generate the dynamic part
		def binding = [
			serviceName : edmiServiceName,
			workingDir	: baseDirectory	//'C:/a/gradle-working-samples/Xxx'
		]
		String response = template.make(binding)
		
		outputBuildSpec = new File('buildRepo-'+edmiServiceName+'.gradle')
		outputBuildSpec.append(response)
	}
}

