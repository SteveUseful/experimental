import numpy as np
import pandas as pd
from faker import Faker
from scipy.stats import truncnorm, bernoulli

fake = Faker()

# Set a random seed for reproducibility
np.random.seed(42)

# Define the number of samples
n_samples = 1000000  # Updated to 1M

# Helper function to generate normally distributed data within specified range
def get_truncated_normal(mean=0, sd=1, low=0, upp=10):
    return truncnorm((low - mean) / sd, (upp - mean) / sd, loc=mean, scale=sd)

# Generate synthetic data using vectorization for efficiency
data = {
    'CustomerID': [fake.uuid4() for _ in range(n_samples)],
    'CustomerAge': np.random.randint(18, 70, n_samples),
    'CustomerIncome': get_truncated_normal(mean=50000, sd=20000, low=20000, upp=150000).rvs(n_samples),
    'CustomerEducationLevel': np.random.choice(['High School', 'Bachelor', 'Master', 'PhD'], p=[0.3, 0.4, 0.2, 0.1], size=n_samples),
    'SupportInteraction': np.random.poisson(1, n_samples),
    'SatisfactionScore': np.random.randint(1, 5, n_samples),
    'UsageFrequency': get_truncated_normal(mean=3, sd=1, low=1, upp=5).rvs(n_samples),
    'FeatureUsageScore': get_truncated_normal(mean=50, sd=20, low=10, upp=100).rvs(n_samples),
    'AccountLifetime': np.random.poisson(24, n_samples),  # Average account lifetime in months
    'ContractRenewal': bernoulli.rvs(0.7, size=n_samples),  # Probability of contract renewal
    'MonthlyCharges': get_truncated_normal(mean=100, sd=20, low=30, upp=200).rvs(n_samples)
}

# Create DataFrame to facilitate vectorized operations for churn calculation
df = pd.DataFrame(data)

# Calculate TotalCharges as a product of MonthlyCharges and AccountLifetime
df['TotalCharges'] = df['MonthlyCharges'] * df['AccountLifetime']

# Calculate ChurnRisk based on various factors
df['ChurnRisk'] = np.clip(
    0.1 + 0.5 * (1 - df['SatisfactionScore'] / 5) +
    0.3 * (1 - df['FeatureUsageScore'] / 100) +
    0.1 * (1 - df['SupportInteraction']) +
    0.2 * (1 - df['ContractRenewal']),
    0, 1
)

# Generate 'Churned' column based on 'ChurnRisk'
df['Churned'] = bernoulli.rvs(df['ChurnRisk'], size=n_samples)

# Generate reasons for churn dynamically based on risk factors
reasons_for_churn = [
    'Product-market fit failure', 'Poor onboarding', 'Delayed Aha moment',
    'Bad customer experience', 'Poor customer service', 'Product pricing plan weak points',
    'Lack of necessary features', 'Poor user experience', 'Competitor offers',
    'Pricing concerns'
]
weights = np.random.dirichlet(np.ones(len(reasons_for_churn)) * 5, size=n_samples)
df['ChurnRiskReason'] = [np.random.choice(reasons_for_churn, p=weight) for weight in weights]

# Save the DataFrame to a CSV file
output_file_path = 'C:/dev/Output/Vectorized_Churn_Data.csv'
df.to_csv(output_file_path, index=False)
print(f"Synthetic data file saved to {output_file_path}")
