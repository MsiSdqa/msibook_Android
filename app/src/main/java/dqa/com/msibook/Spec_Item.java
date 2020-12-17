package dqa.com.msibook;

public class Spec_Item {

    String ComponentName;

    String ComponentContent;


    public Spec_Item(String ComponentName, String ComponentContent) {
        this.ComponentName = ComponentName;

        this.ComponentContent = ComponentContent;


    }

    public String GetComponentName() {
        return this.ComponentName;
    }

    public String GetComponentContent() {
        return this.ComponentContent;
    }

}

