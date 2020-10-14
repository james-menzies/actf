# AutoComplete TextField

The AutoComplete TextField control allows end users to select an item from an arbitrarily large list. This is achieved by partially typing the desired selection into a text field, whilst the control dynamically filters the results based on the user input. The user is able to navigate up and down through the filtered suggestions, and select the desired object with the 'TAB' key.

Whilst Java FX has robust support for selecting items from a list, whether it be a `ListView` or employing a set of `CheckBox` or `RadioButton` objects, it becomes cumbersome to cleanly navigate the options as the list grows infinitely large. That's where the AutoComplete TextField comes in, as it can account for really large lists whilst keeping the interface clean.

## How to Install

## Instructions

> I highly recommend anyone using this control have a solid understanding of the Property binding system in Java FX. [I would highly recommend this article by Oracle](https://docs.oracle.com/javafx/2/binding/jfxpub-binding.htm#:~:text=JavaFX%20properties%20are%20often%20used,in%20a%20variety%20of%20applications.) if you're relatively new to the Java FX platform.

### First Steps

The AutoComplete TextField extends from a regular TextField, meaning you can interact with it like any normal Java FX control. In order to use the control, you first need to import it with the following line:
```java
import com.redbrickhut.actf.AutoCompleteTextField;
```

### Quickstart: A Simple Example

In order to instantiate an ACTF, it requires as a minimum a list of objects for the user to select from. It takes an [`ObservableList`](https://openjfx.io/javadoc/14/javafx.base/javafx/collections/ObservableList.html) of objects to do so. In this short example, we are going to:

* Create a simple class `Person`.
* Populate an `ObservableList` of `Person` objects.
* Instantiate the `AutoCompleteTextField` by passing our newly created list.
* Learn how to retrieve the selected value for integration into the application's logic.

Firstly, let's create our basic Person class:
```java
public class Person {
    
    private String firstName;
    private String lastName;

    public Person(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }
    // getters and setters
    
    


}
```


## How to use



## Demo