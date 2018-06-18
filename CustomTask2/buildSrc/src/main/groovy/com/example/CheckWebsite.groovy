package com.example

//always need to import these two classes for a custom task
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

class CheckWebsite extends DefaultTask {

    String url = 'http://www.google.com'

    @TaskAction
    void checkWebsite() {
        // check the given website by using the url
        try {
                Document doc = Jsoup.connect(url).get();
                String title = doc.title();
                println 'url ==> ' + url + ' title ==> ' + title
        } catch (IOException e) {
                e.printStackTrace();
        }
    }
}