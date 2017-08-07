# Kommander

**Kommander** is a *Java* asynchronous [Command Pattern](https://en.wikipedia.org/wiki/Command_pattern) implementation, specially recommended
to solve **Android UI Thread Issue**.

## Usage 

**Kommander** is designed to be really simple to use. First of all, you need to create a ```Kommander``` instance. 

```java
Kommander kommander = Kommander.getInstance();
```

That was easy, right? Now, let's launch an asynchronous execution.  

#### Kommands

```java
kommander.makeKommand(() -> interactor.searchMovie("Titanic"))
    .setOnCompleted(this::paintMovies)
    .kommand();
```

This example is executing an asynchronous search of a movie and releases the result on ```paintMovies()``` when all has worked fine.  

  + Let's see each step in detail:
    
	* Making a **Kommand**
	  
    ```java
    Kommand<List<Movie>> kommand = kommander.makeKommand(new Action<List<Movie>>() {
        @Override
        public List<Movie> action() throws Throwable {
            return interactor.searchMovie("Titanic");
        }
    });	
    ```
    
    A ```Kommand``` is an asynchronous context builder of an ```Action```, where it can be defined like a piece of code to be executed in the future 
	that returns a value or throws an exception.  
    
    * Setting up the callback **Response**
    
    ```java
    kommand = kommand.setOnCompleted(new Response.OnCompleted<List<Movie>>() {
        @Override
        public void onCompleted(List<Movie> response) {
            paintMovies(response);
        }    
    });
    
    // ... 
    
    private void paintMovies(List<Movie> movies) {
    // do something
    }
    ```
    
	In this step a ```Response.OnCompleted``` is being set up, that is who is going to handle the ```Action``` result. Also, in a similar way, is
	possible setting up the ```Response.OnError``` when something is not going fine in the ```Action``` execution.
    
	```java
    kommand = kommand.setOnError(new Response.OnError() {
        @Override
        public void onError(Throwable error) {
            showMessage("Something failed!");
        }
    });
    ```  
	
    * Launch the **Kommand**
    
    ```java
    KommandToken token = kommand.kommand();
    ``` 
    
    When a ```Kommand``` is fully defined, it can be launched to an asynchronous execution with ```kommand()```.  
    A ```KommandToken``` is returned, you can use it to cancel the ```Kommand``` execution. A canceled ```Kommand```, will never be executed if the 
    execution has not started yet, try to stop the execution if that is running, or at least, the response is not delivered when the execution finish.  

  + **Delay** You can define a delay time, in milliseconds, to kommand execution.
  
    ```java
    long millisecondDelay = 60_000L;
    kommand = kommand.delay(millisecondDelay); 
    ``` 
    
  + A **Deliverer** is just a way to define how and when ```Kommander``` will deliver the result of an asynchronous execution.   
    
    ```java
    kommand.setDelivered(new Deliverer() {
        @Override
        public void deliver(Runnable runnable) {
            // How do you want to deliver the response? Your code here! 
        }
    });
    ```
    
    For instance, using ```Kommander``` on Android, you could use the next ```Deliverer``` to release the results on the Android's UI Thread.
    
    ```java
    @Provides(singleton = true)
    Deliverer provideAndroidDeliverer() {
        Handler handler = new Handler(Looper.getMainLooper());
        return handler::post;
    }
    ```
 
#### KommandTokenBox

  When you make ```Kommand.kommand()``` you receive a ```KommandToken``` instance to take control of the kommand cancellation with 
  ```KommandToken.cancel()```, but often managing these tokens is a few unpleasant. So you can take advance of ```KommandTokenBox``` to easy the 
  cancel control.
  
  + Add tokens to box
    
    ```java
    KommandTokenBox box = new KommandTokenBox();    // TokenBox
	Kommand<Integer> kommand = kommander.makeKommand(() -> 1);

    // Explicit appendToken
	KommandToken token = kommand.kommand();
    box.append(token);                             // AppendToken to the box
	box.append(token, "VIEW_SCOPE_TAG");           // AppendToken to the box with tag

    // Implicit appendToken
    kommand.kommand(box);                          // AppendToken to the box
    kommand.kommand(box, "VIEW_SCOPE_TAG");        // AppendToken to the box with tag
    ```
    
  + Cancelling tokens

    ```java
    box.cancel("VIEW_SCOPE_TAG");                  // Cancel previously appended tokens with this tag 
    box.cancelAll();                               // Cancel all tokens appended tokens
    ```

## Download

Kommander is available on jcenter. Please ensure that you are using the latest version
checking <a href="https://bintray.com/wokdsem/maven/kommander/view">here</a>.

**Maven:**

```xml
<dependency>
  <groupId>com.wokdsem.kommander</groupId>
  <artifactId>kommander</artifactId>
  <version>1.3.0</version>
</dependency>
```

**Gradle:**

```groovy
compile 'com.wokdsem.kommander:kommander:1.3.0'
```

## License

	Copyright 2017 Wokdsem

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

