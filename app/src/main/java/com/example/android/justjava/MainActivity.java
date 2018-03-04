package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;


/**
 * This app displays an order form to order coffee.
 * An Intent is used to send the info to an email app to display the order summary.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void increment(View view) {
        if (quantity == 100) {
            // Show an error message as a toast
            Toast.makeText(this, "You cannot have more than 100 coffees", Toast.LENGTH_SHORT).show();
            // Exit this method early because there's nothing left to do
            return;
        }
        quantity += 1;
        displayQuantity(quantity);
    }

    public void decrement(View view) {
        if (quantity == 1) {
            // Show an error message as a toast
            Toast.makeText(this, "You cannot have less than 1 coffee", Toast.LENGTH_SHORT).show();
            // Exit this method early because there's nothing left to do
            return;
        }
        quantity -= 1;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        //Gets name input to display on order summary
        EditText nameField = (EditText) findViewById(R.id.name_field);
        String name = nameField.getText().toString();

        //Figures out if the user wants whipped cream
        CheckBox whippedCreamCheckbox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckbox.isChecked();

        //Figures out if the user wants chocolate
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        //Calls method to calculate the price and displays the Order Summary
        int price = calculatePrice(hasWhippedCream, hasChocolate);
        String priceMessage = createOrderSummary(name, price, hasWhippedCream, hasChocolate);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_summary_email_subject, name));
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Calculates the price of the order.
     *
     * @param addWhippedCream is whether or not the user wants whipped cream
     * @param addChocolate is whether or not the user wants chocolate
     * @return total price
     */
    private int calculatePrice(boolean addWhippedCream, boolean addChocolate) {
        int basePrice = 5;

        // Adds $1 if user wants whipped cream
        if (addWhippedCream) {
            basePrice += 1;
        }

        // Adds $2 is user wants chocolate
        if (addChocolate) {
            basePrice += 2;
        }
        //Calculate total price by multiplying by quantity
        return quantity * basePrice;
    }

    /**
     * Create summary of the order.
     *
     * @param name of user
     * @param price of the order
     * @param addWhippedCream shows whether or not whipped cream is added
     * @param addChocolate shows whether or not chocolate is added
     * @return text summary
     */
    private String createOrderSummary(String name, int price, boolean addWhippedCream, boolean addChocolate) {
        String priceMessage = getString(R.string.name_field, name);
        priceMessage += "\n" + getString(R.string.add_whipped_cream, addWhippedCream);
        priceMessage += "\n" + getString(R.string.add_chocolate, addChocolate);
        priceMessage += "\n" + getString(R.string.quantity_summary, quantity);
        priceMessage += "\n" + getString(R.string.total,
                NumberFormat.getCurrencyInstance().format(price));
        priceMessage += "\n" + getString(R.string.thank_you);
        return priceMessage;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int numberOfCoffees) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + numberOfCoffees);
    }
}