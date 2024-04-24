import pandas as pd
import numpy as np
import sys

np.random.seed(42)
n_samples = 1000000

def generate_performance_score(usage_frequency, satisfaction_score, support_tickets):
  
    # Adding a small constant (e.g., 0.0001) to avoid division by zero
    performance = usage_frequency * satisfaction_score / (np.log1p(support_tickets) + 0.0001)
    return np.clip(performance, 0, 100)

def generate_value_score(satisfaction_score, engagement_score, usage_frequency):
    return satisfaction_score * engagement_score * usage_frequency

usage_frequency_map = {'Daily': 3, 'Weekly': 2, 'Monthly': 1}
product_usage_frequency = np.random.choice(['Daily', 'Weekly', 'Monthly'], size=n_samples)
usage_frequency_numeric = np.array([usage_frequency_map[freq] for freq in product_usage_frequency])

data = {
    "Customer ID": np.arange(1, n_samples + 1),
    "Churn": np.random.choice([0, 1], size=n_samples, p=[0.7, 0.3]),
    "Tenure": np.random.randint(1, 60, size=n_samples),
    "Number of Support Tickets": np.random.randint(0, 10, size=n_samples),
    "Customer Satisfaction Score": np.random.randint(1, 11, size=n_samples),
    "Product Usage Frequency": product_usage_frequency,
    "Recent Activity Score": np.random.random(size=n_samples),
    "Engagement Score": np.random.random(size=n_samples),
    "Age": np.random.randint(18, 70, size=n_samples),
    "Gender": np.random.choice(['Male', 'Female', 'Other'], size=n_samples),
    "Income Level": np.random.choice(['Low', 'Medium', 'High'], size=n_samples),
    "Geographic Location": np.random.choice(['North', 'South', 'East', 'West'], size=n_samples),
    "Account Age": np.random.randint(1, 120, size=n_samples),
    "Subscription Type": np.random.choice(['Basic', 'Premium'], size=n_samples),
    "Contract Type": np.random.choice(['Month-to-month', 'One year', 'Two year'], size=n_samples),
    "Payment Method": np.random.choice(['Credit card', 'PayPal', 'Bank transfer'], size=n_samples),
    "Monthly Charges": np.random.uniform(10, 100, size=n_samples).round(2),
    "Total Charges": np.random.uniform(100, 10000, size=n_samples).round(2),
    "Last Interaction": np.random.randint(0, 365, size=n_samples),
    "Number of Interactions": np.random.randint(1, 100, size=n_samples),
    "Data Usage": np.random.uniform(0, 1000, size=n_samples).round(2),
    "Feature Utilization": np.random.choice(['Yes', 'No'], size=n_samples),
    "Upgrade/Downgrade Status": np.random.choice(['Upgraded', 'Downgraded', 'No change'], size=n_samples),
    "Cancellation Attempts": np.random.choice([0, 1], size=n_samples),
    
    # New features based on previous discussions
    "Average Resolution Time": np.random.uniform(1, 10, size=n_samples).round(2),  # Simulated average resolution time in hours
    "Value Score": generate_value_score(
        np.random.randint(1, 11, size=n_samples),
        np.random.random(size=n_samples),
        usage_frequency_numeric
    ),
    # Existing performance score
    "Product Performance Score": generate_performance_score(
        usage_frequency_numeric,
        np.random.randint(1, 11, size=n_samples),
        np.random.randint(0, 10, size=n_samples)
    )
}

df = pd.DataFrame(data)
csv_file_path = "C:/dev/Output/Extended_Sample_Customer_Churn_Data.csv"
df.to_csv(csv_file_path, index=False)
print(f"File saved to {csv_file_path}")



print("Python interpreter in use:", sys.executable)
