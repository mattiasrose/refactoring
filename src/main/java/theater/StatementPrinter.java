package theater;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

/**
 * This class generates a statement for a given invoice of performances.
 */
public class StatementPrinter {

    private final Invoice invoice;
    private final Map<String, Play> plays;

    /**
     * Construct a new statement printer for the given invoice and plays.
     *
     * @param invoice the invoice containing the performances
     * @param plays   the plays indexed by their identifier
     */
    public StatementPrinter(Invoice invoice, Map<String, Play> plays) {
        this.invoice = invoice;
        this.plays = plays;
    }

    /**
     * Returns a formatted statement of the invoice associated with this printer.
     *
     * @return the formatted statement
     * @throws RuntimeException if one of the play types is not known
     */
    public String statement() {
        final StringBuilder result =
                new StringBuilder(
                        String.format("Statement for %s%n", invoice.getCustomer()));

        for (Performance performance : invoice.getPerformances()) {
            // print line for this order
            result.append(
                    String.format("  %s: %s (%s seats)%n",
                            getPlay(performance).getName(),
                            usd(getAmount(performance)),
                            performance.getAudience()));
        }

        result.append(
                String.format("Amount owed is %s%n", usd(getTotalAmount())));
        result.append(
                String.format("You earned %s credits%n", getTotalVolumeCredits()));

        return result.toString();
    }

    /**
     * Get the play for the given performance.
     *
     * @param performance the performance
     * @return the corresponding play
     */
    private Play getPlay(Performance performance) {
        return plays.get(performance.getPlayID());
    }

    /**
     * Compute the amount owed for a single performance.
     *
     * @param performance the performance
     * @return the amount in cents
     * @throws RuntimeException if play type is unknown
     */
    private int getAmount(Performance performance) {
        int result;
        final Play play = getPlay(performance);

        switch (play.getType()) {
            case "tragedy":
                result = Constants.TRAGEDY_BASE_AMOUNT;
                if (performance.getAudience() > Constants.TRAGEDY_AUDIENCE_THRESHOLD) {
                    result += Constants.TRAGEDY_OVER_BASE_CAPACITY_PER_PERSON
                            * (performance.getAudience()
                            - Constants.TRAGEDY_AUDIENCE_THRESHOLD);
                }
                break;
            case "comedy":
                result = Constants.COMEDY_BASE_AMOUNT;
                if (performance.getAudience() > Constants.COMEDY_AUDIENCE_THRESHOLD) {
                    result += Constants.COMEDY_OVER_BASE_CAPACITY_AMOUNT
                            + Constants.COMEDY_OVER_BASE_CAPACITY_PER_PERSON
                            * (performance.getAudience()
                            - Constants.COMEDY_AUDIENCE_THRESHOLD);
                }
                result += Constants.COMEDY_AMOUNT_PER_AUDIENCE
                        * performance.getAudience();
                break;
            default:
                throw new RuntimeException(
                        String.format("unknown type: %s", play.getType()));
        }
        return result;
    }

    /**
     * Compute the volume credits earned for a single performance.
     *
     * @param performance the performance
     * @return the number of volume credits
     */
    private int getVolumeCredits(Performance performance) {
        int result = 0;

        result += Math.max(
                performance.getAudience() - Constants.BASE_VOLUME_CREDIT_THRESHOLD, 0);
        if ("comedy".equals(getPlay(performance).getType())) {
            result += performance.getAudience() / Constants.COMEDY_EXTRA_VOLUME_FACTOR;
        }

        return result;
    }

    /**
     * Compute the total volume credits over all performances.
     *
     * @return total volume credits
     */
    private int getTotalVolumeCredits() {
        int result = 0;
        for (Performance performance : invoice.getPerformances()) {
            result += getVolumeCredits(performance);
        }
        return result;
    }

    /**
     * Compute the total amount owed over all performances.
     *
     * @return the total amount in cents
     */
    private int getTotalAmount() {
        int result = 0;
        for (Performance performance : invoice.getPerformances()) {
            result += getAmount(performance);
        }
        return result;
    }

    /**
     * Format an amount in cents as a US currency string.
     *
     * @param amountInCents the amount in cents
     * @return the formatted amount
     */
    private String usd(int amountInCents) {
        final NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        final double amount = (double) amountInCents / Constants.PERCENT_FACTOR;
        return formatter.format(amount);
    }
}
