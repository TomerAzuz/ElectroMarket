package com.ElectroMarket.mailservice.constants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EmailConstants {
    public static final String ELECTROMARKET_EMAIL = "support@electromarket.site";

    public static final String CONFIRMATION_EMAIL_SUBJECT = "Your Order is Confirmed! 🎉";
    public static final String CONFIRMATION_EMAIL_BODY_TEMPLATE =
            """
                    Dear Customer,

                    We are thrilled to inform you that your order has been successfully completed! Thank you for choosing [Your Company Name] for your purchase.

                    Order Details:
                    Order ID: [Order ID]
                    Date: [Order Completion Date]

                    If you have any questions or concerns regarding your order, feel free to reach out to our customer support team at [Customer Support Email or Phone Number].

                    Thank you again for choosing [Your Company Name]! We appreciate your business.

                    Best Regards,
                    [Your Company Name]
            """;

    public static final String CONTACT_FORM_EMAIL =
            """
                Hello,
    
                You have received a new message:
    
                Name: [Name]
                Email: [Email]
                Subject: [Subject]
    
                Message: [Message]
            """;

    public static String formatConfirmationEmailBody(Long orderID, String supportContact, String companyName) {
        return CONFIRMATION_EMAIL_BODY_TEMPLATE
                .replace("[Order ID]", orderID.toString())
                .replace("[Order Completion Date]", getCurrentDate())
                .replace("[Customer Support Email or Phone Number]", supportContact)
                .replace("[Your Company Name]", companyName);
    }

    public static String formatContactFormMessage(String sender, String email, String subject, String message) {
        return CONTACT_FORM_EMAIL
                .replace("[Name]", sender)
                .replace("[Email]", email)
                .replace("[Subject]", subject)
                .replace("[Message]", message);
    }

    public static String getCurrentDate() {
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentDate.format(formatter);
    }
}
