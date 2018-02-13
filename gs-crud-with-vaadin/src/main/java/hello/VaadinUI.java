package hello;

import com.vaadin.annotations.Theme;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * Created by Administrator on 2/13/2018.
 */
@SpringUI
@Theme("valo")
public class VaadinUI extends UI {
    private final CustomerRepository repo;
    private final CustomerEditor editor;
    final Grid<Customer> grid;
    final TextField filter;
    private final Button addNewBtn;

    @Autowired
    public VaadinUI(CustomerRepository repo,CustomerEditor editor) {
        this.repo = repo;
        this.editor = editor;
        editor.setVisible(false);
        this.grid = new Grid<>(Customer.class);
        this.filter = new TextField();
        this.addNewBtn = new Button("New customer", FontAwesome.PLUS);
    }
    @Override
    protected void init(VaadinRequest request) {

        HorizontalLayout actions = new HorizontalLayout(filter,addNewBtn);
        VerticalLayout mainLayout = new VerticalLayout(actions,grid,editor);
        setContent(mainLayout);

        filter.setPlaceholder("Filter by last name");
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e->listCustomers(e.getValue()));

        grid.asSingleSelect().addValueChangeListener(e->{
            editor.editCustomer(e.getValue());
        });

        addNewBtn.addClickListener(e->editor.editCustomer(new Customer("","")));

        editor.setChangeHandler(()->{
            editor.setVisible(false);
            listCustomers(filter.getValue());
        });

        listCustomers("");
    }

    private void listCustomers(String filterText) {
        if(StringUtils.isEmpty(filterText)) {
            grid.setItems(repo.findAll());
        }
        else
        {
            grid.setItems(repo.findByLastNameContainingIgnoreCase(filterText));
        }

    }
}
