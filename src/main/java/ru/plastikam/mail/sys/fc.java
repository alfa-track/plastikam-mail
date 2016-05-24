package ru.plastikam.mail.sys;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.plastikam.mail.ui.AbstractBacking;

@Service
@Scope(scopeName = "view")
public class fc extends AbstractBacking {
    public String br(String s) {
        return FC.br(s);
    }

}
