import numpy as np
import pandas as pd

np.random.seed(42)
n_samples = 500000

def generate_performance_score(usage_frequency, satisfaction_score, support_tickets):
    performance = (3 * usage_frequency * satisfaction_score) - (2 * np.log1p(support_tickets))
    noise = np.random.normal(0, 1, size=len(performance))  # Adding noise for realism
    min_perf = performance.min()
    max_perf = performance.max()
    normalized_performance = 1 + ((performance + noise - min_perf) / (max_perf - min_perf)) * 9
    return np.round(normalized_performance)

def generate_value_score(satisfaction, engagement, usage):
    value = (5 * satisfaction) + (3 * engagement) + (2 * usage)
    noise = np.random.normal(0, 0.5, size=len(value))  # Less variance in noise for stability
    min_val = value.min()
    max_val = value.max()
    normalized_value = 1 + ((value + noise - min_val) / (max_val - min_val)) * 9
    return np.round(normalized_value)

usage_frequency_map = {'Daily': 3, 'Weekly': 2, 'Monthly': 1}
product_usage_frequency = np.random.choice(list(usage_frequency_map.keys()), n_samples, p=[0.6, 0.3, 0.1])
usage_numeric = np.array([usage_frequency_map[freq] for freq in product_usage_frequency])

support_tickets = np.random.poisson(lam=3, size=n_samples)  # Reduced lambda for more realistic ticket numbers

# Creating synthetic data with embedded patterns
data = {
    "Customer ID": np.arange(1, n_samples + 1),
    "Churn": np.random.choice([0, 1], n_samples, p=[0.85, 0.15]),
    "Tenure": np.random.exponential(20, n_samples).astype(int),
    "Number of Support Tickets": support_tickets,
    "Customer Satisfaction Score": np.random.choice(np.arange(1, 11), n_samples, p=[0.05, 0.05, 0.1, 0.1, 0.2, 0.15, 0.15, 0.1, 0.05, 0.05]),
    "Product Usage Frequency": product_usage_frequency,
    "Recent Activity Score": np.round(1 + 9 * np.random.beta(2, 5, n_samples)),
    "Engagement Score": np.round(1 + 9 * np.random.beta(5, 2, n_samples)),
    "Age": np.random.randint(18, 70, n_samples),
    "Gender": np.random.choice(['Male', 'Female', 'Other'], size=n_samples),
    "Income Level": np.random.choice(['Low', 'Medium', 'High'], size=n_samples, p=[0.2, 0.5, 0.3]),
    "Geographic Location": np.random.choice(['North', 'South', 'East', 'West'], size=n_samples),
    "Account Age": np.random.randint(1, 120, size=n_samples),
    "Subscription Type": np.random.choice(['Basic', 'Premium'], size=n_samples),
    "Contract Type": np.random.choice(['Month-to-month', 'One year', 'Two year'], size=n_samples, p=[0.5, 0.25, 0.25]),
    "Payment Method": np.random.choice(['Credit card', 'PayPal', 'Bank transfer'], size=n_samples),
    "Monthly Charges": np.random.triangular(10, 50, 100, size=n_samples).round(2),
    "Total Charges": np.random.uniform(100, 10000, size=n_samples).round(2),
    "Last Interaction": np.random.randint(0, 365, size=n_samples),
    "Number of Interactions": np.random.poisson(2, size=n_samples),
    "Data Usage": np.random.uniform(0, 1000, size=n_samples).round(2),
    "Feature Utilization": np.random.choice(['Yes', 'No'], size=n_samples, p=[0.7, 0.3]),
    "Upgrade/Downgrade Status": np.random.choice(['Upgraded', 'Downgraded', 'No change'], size=n_samples, p=[0.1, 0.1, 0.8]),
    "Cancellation Attempts": np.random.binomial(1, 0.05, size=n_samples)
}

# Enhanced metrics
data['Product Performance Score'] = generate_performance_score(
    usage_numeric, data['Customer Satisfaction Score'], data['Number of Support Tickets']
)
data['Value Score'] = generate_value_score(
    data['Customer Satisfaction Score'], data['Engagement Score'], usage_numeric
)

# Convert to DataFrame
df = pd.DataFrame(data)

# Path where the CSV will be saved
csv_file_path = "C:/dev/Output/Extended_Customer_Churn_Data.csv"

# Save to CSV, without the index
df.to_csv(csv_file_path, index=False)
print(f"File saved to {csv_file_path}")
