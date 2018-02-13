package hello;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2/13/2018.
 */
@SpringComponent
@UIScope
public class CustomerEditor extends VerticalLayout {
    private final CustomerRepository repository;

    private Customer customer;

    TextField firstName = new TextField("First Name");
    TextField lastName = new TextField("Last Name");

    Button save = new Button("Save", FontAwesome.SAVE);
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete",FontAwesome.TRASH_O);

    CssLayout actions = new CssLayout(save,cancel,delete);

    Binder<Customer> binder = new Binder<>(Customer.class);

    @Autowired
    public CustomerEditor(CustomerRepository repository) {
        this.repository = repository;

        addComponents(firstName,lastName,actions);

        // bind using naming convention
        binder.bindInstanceFields(this);

        // Configure and style components
        setSpacing(true);
        actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        // wire buttons to save, delete, and reset
        save.addClickListener(e->repository.save(customer));
        delete.addClickListener(e->repository.delete(customer));
        cancel.addClickListener(e->editCustomer(customer));
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editCustomer(Customer c) {
        if(c==null) {
            setVisible(false);
            return;
        }

        final boolean persisted = c.getId() != null;

        if(persisted) {
            customer = repository.findOne(c.getId());
        }
        else
        {
            customer = c;
        }

        cancel.setVisible(persisted);

        /*
            bind customer properties to similarly named fields
            could also use annotation or "manual binding" or programatically
            moving values from fields to entities before saving
         */
        binder.setBean(customer);

        setVisible(true);

        save.focus();
        firstName.selectAll();
    }

    public void setChangeHandler(ChangeHandler h) {
        save.addClickListener(e->h.onChange());
        delete.addClickListener(e->h.onChange());
    }
}
