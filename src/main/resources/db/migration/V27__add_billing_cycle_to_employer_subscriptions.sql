-- Add billing cycle column to employer_subscriptions
ALTER TABLE employer_subscriptions ADD COLUMN billing_cycle billing_cycle DEFAULT 'MONTHLY';
