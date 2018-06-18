import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class MyGradleTask extends DefaultTask {
  def key = 'hello'
  def value
  def somethingelse = '!'
 
  @TaskAction
  def myTaskAction() {
	def msgString = "${key} ${value} ${somethingelse}"
	println msgString
  }
}