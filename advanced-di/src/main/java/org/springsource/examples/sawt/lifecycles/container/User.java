package org.springsource.examples.sawt.lifecycles.container;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springsource.examples.sawt.lifecycles.container.impl.AutoToStringRequired;

@Component
@Scope("prototype")
public class User implements AutoToStringRequired {

    private String firstName,
            lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
