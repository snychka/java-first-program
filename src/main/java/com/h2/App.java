package com.h2;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }

    public static int doubleTheNumber(int number) {

        return number*2;
    }

    /*
    Next, create a new method called `add()`.
    This method should be `private`, `static`,
    and should have a return type of `int`.
    The method should take one parameter as its input.
    The parameter name should be `numbers`, and should be of type `int[]`.

     */

    private static int add(int[] numbers) {
        /*For the implementation of `add()` method,
        create a variable called `sum`, and initialize its value to `0`.
        Then, using a `for` loop over `numbers`,
        add every item in the `numbers` array to the `sum` variable. Finally, return `sum`.*/
        var sum = 0;
        for (var n: numbers) {
            sum += n;
        }
        return sum;
    }
}
