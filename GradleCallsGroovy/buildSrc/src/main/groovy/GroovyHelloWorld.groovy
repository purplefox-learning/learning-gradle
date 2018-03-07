
class GroovyHelloWorld {

	String worldName

	GroovyHelloWorld(name) {
		worldName = name
	}
	
	void sayHello() {
		println 'say hello from world '+worldName
	}
	void saySomething(message) {
		println 'say '+message+' from world '+worldName
	}
}

