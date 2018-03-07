package com.vogella.gradle.exampleplugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class MyPlugin implements Plugin<Project> {

	//entry point - apply() method
	//the project is passed in as argument, for the plugin to access/manipulate/configure when needed
	
    @Override
    public void apply(Project p) {
		//add a task called "javaTask"
        p.task("javaTask");
    }

}