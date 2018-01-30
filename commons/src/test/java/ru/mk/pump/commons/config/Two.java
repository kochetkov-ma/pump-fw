package ru.mk.pump.commons.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Two {

    @Property(value = "one")
    private String one;

    @Property(value = "two")
    private String two;

}
