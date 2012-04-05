package org.springsource.examples.sawt.web.gwt.client.widgets;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import org.springsource.examples.sawt.web.gwt.client.Messages;
import org.springsource.examples.sawt.web.gwt.client.entities.CustomerDto;
import org.springsource.examples.sawt.web.gwt.client.service.GwtCustomerServiceAsync;

public class CustomerPanel extends Composite {

    interface CustomerUiBinder extends UiBinder<Widget, CustomerPanel> {
    }

    private GwtCustomerServiceAsync gwtCustomerService;
    private Messages messages;
    private CustomerUiBinder uiBinder = GWT.create(CustomerUiBinder.class);
    private CustomerDto customerDto;

    @UiField
    Button createCustomerButton;
    @UiField
    TextBox firstName;
    @UiField
    TextBox lastName;
    @UiField
    Button updateButton;
    @UiField
    Label firstNameLabel;
    @UiField
    Label lastNameLabel;
    @UiField
    Button searchCustomerButton;
    @UiField
    TextBox customerId;
    @UiField
    Button cancelButton;

    public void setCustomerDto(CustomerDto customerDto) {
        this.customerDto = customerDto;
    }

    /**
     * called from the controller to edit customer records.
     */
    public void editCustomer(CustomerDto customerDto) {
        if (customerDto != null) {
            firstName.setText(customerDto.getFirstName());
            lastName.setText(customerDto.getLastName());
        } else {
            firstName.setText("");
            lastName.setText("");
        }
        updateButton.setText(messages.updateCustomer());
        setEditorEnabled(true);
    }

    private void setEditorEnabled(boolean enabled) {
        searchCustomerButton.setEnabled(!enabled);
        customerId.setEnabled(!enabled);

        FocusWidget[] widgets = {firstName, lastName, updateButton, cancelButton};
        for (FocusWidget focusWidget : widgets)
            focusWidget.setEnabled(enabled);
    }

    public CustomerPanel(GwtCustomerServiceAsync cs, Messages messages) {
        this.gwtCustomerService = cs;
        this.messages = messages;
        initWidget(uiBinder.createAndBindUi(this));

        createCustomerButton.setText(messages.createCustomer());
        updateButton.setText(messages.save());
        firstNameLabel.setText(messages.firstName());
        lastNameLabel.setText(messages.lastName());
        searchCustomerButton.setText(messages.searchCustomerById());
        cancelButton.setText(messages.cancel());
        setEditorEnabled(false);
    }

    private void showDialog(String msg) {
        Window.alert(msg);
    }

    @UiHandler("cancelButton")
    public void cancelForm(ClickEvent e) {
        createCustomer();
        setEditorEnabled(false);
    }

    @UiHandler("createCustomerButton")
    public void setupFormForNewCustomerRecord(ClickEvent evt) {
        createCustomer();

        setEditorEnabled(true);
    }

    private void createCustomer() {
        setCustomerDto(new CustomerDto());
        updateButton.setText(messages.save());
        firstName.setText(customerDto.getFirstName());
        lastName.setText(customerDto.getLastName());
    }

    @UiHandler("updateButton")
    public void updateOrSave(ClickEvent evt) {
        customerDto.setFirstName(firstName.getText());
        customerDto.setLastName(lastName.getText());

        if (!customerDto.isSaved()) {
            gwtCustomerService.createCustomer(customerDto.getFirstName(), customerDto.getLastName(), new AsyncCallback<CustomerDto>() {
                public void onFailure(Throwable caught) {
                    error(caught);
                }

                public void onSuccess(CustomerDto result) {
                    setCustomerDto(result);
                    editCustomer(result);
                    showDialog(messages.customerCreated());
                }
            });
        } else {
            gwtCustomerService.updateCustomer(customerDto.getId(), customerDto.getFirstName(), customerDto.getLastName(), new AsyncCallback<Void>() {
                public void onFailure(Throwable throwable) {
                    error(throwable);
                }

                public void onSuccess(Void aVoid) {
                    createCustomer();
                    showDialog(messages.customerUpdated());
                }
            });
        }
    }

    private void error(Throwable throwable) {
        Window.alert(messages.fail() + throwable.toString());
    }

    @UiHandler("searchCustomerButton")
    public void onSearchClickEvent(ClickEvent clickEvent) {
        final String cid = customerId.getText();
        gwtCustomerService.getCustomerById(Long.parseLong(cid), new AsyncCallback<CustomerDto>() {
            public void onFailure(Throwable throwable) {
                error(throwable);
            }

            public void onSuccess(CustomerDto customerDto) {
                setCustomerDto(customerDto);
                editCustomer(customerDto);
            }
        });
    }
}