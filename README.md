# Cascading Fors

Utility class to make cascading fors more readable. 

Instead of 

```java
    for (int i=0; i<=10; i++) {
        for (int j=0; j<=10; j++) {
            System.out.println(i + " " + j + " " + x/100 + "%");
        }
    }
```
     
do 

```java    
    For f1 = new For(0, 10, 1);
    For f2 = new For(0, 10, 1);

    CascadingFors fs = new CascadingFors(f1, f2);
    fs.run(() -> {
        System.out.println(f1.value + " " + f2.value + " " + fs.fmtProgress());
    });
```
    