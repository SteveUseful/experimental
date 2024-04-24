import numpy as np
import pandas as pd
from faker import Faker
from scipy.stats import truncnorm

fake = Faker()

# Set a random seed for reproducibility
np.random.seed(42)

# Define the number of samples
n_samples = 3000000  # Updated to 3M

# Helper function to generate normally distributed data within specified range
def get_truncated_normal(mean=0, sd=1, low=0, upp=10):
    return truncnorm((low - mean) / sd, (upp - mean) / sd, loc=mean, scale=sd)

# Generate synthetic data using vectorization for efficiency
data = {
    'CustomerID': [fake.uuid4() for _ in range(n_samples)],
    'ProductCustomerFitScore': get_truncated_normal(mean=0.5, sd=0.2, low=0, upp=1).rvs(n_samples),
    'OnboardingCompletion': get_truncated_normal(mean=0.7, sd=0.15, low=0, upp=1).rvs(n_samples),
    'CustomerServiceRating': get_truncated_normal(mean=3, sd=1.5, low=0, upp=5).rvs(n_samples),
    'ValueForMoneyRating': get_truncated_normal(mean=3.5, sd=1, low=0, upp=5).rvs(n_samples),
    'ReportedBugs': np.random.poisson(2, n_samples),
    'PaymentIssuesLastYear': np.random.poisson(0.5, n_samples),
    'FeatureRequests': np.random.poisson(1, n_samples),
}

# Create DataFrame to facilitate vectorized operations for churn calculation
df = pd.DataFrame(data)
churn_probability = (
    0.2 + 0.5 * (1 - df['ProductCustomerFitScore']) +
    0.3 * (1 - df['OnboardingCompletion']) +
    0.4 * (1 - df['CustomerServiceRating'] / 5) +
    0.2 * (1 - df['ValueForMoneyRating'] / 5) +
    0.1 * df['ReportedBugs'] / 5 +
    0.2 * df['PaymentIssuesLastYear']
)
df['Churned'] = np.random.binomial(1, p=np.clip(churn_probability, 0, 0.95))

# Generate reasons for churn dynamically based on risk factors
reasons_for_churn = [
    'Bad product-customer fit', 'Ineffective onboarding', 'Poor customer service',
    'Lack of perceived value', 'Unresolved product bugs', 'Payment issues',
    'Lack of necessary features', 'Poor user experience', 'Competitor offers',
    'Pricing concerns'
]
weights = np.random.dirichlet(np.ones(10) * 5, size=n_samples)
df['ChurnRiskReason'] = [np.random.choice(reasons_for_churn, p=weight) for weight in weights]

# Save the DataFrame to a CSV file
output_file_path = 'C:/dev/Output/Vectorized_Churn_Data.csv'
df.to_csv(output_file_path, index=False)
print(f"Synthetic data file saved to {output_file_path}")
