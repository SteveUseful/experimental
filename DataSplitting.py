import pandas as pd
from sklearn.model_selection import train_test_split

# Load your dataset
df = pd.read_csv('C:/dev/Output/Vectorized_Churn_Data.csv')  # Path to your 3 million rows dataset

# Define features and target variable
X = df.drop('Churned', axis=1)  # Assuming 'Churned' is your target variable
y = df['Churned']

# Split the data into training and testing sets
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=42)  # 70% training, 30% testing

# Save the training and testing data
X_train.to_csv('C:/dev/Output/X_train.csv', index=False)
X_test.to_csv('C:/dev/Output/X_test.csv', index=False)
y_train.to_csv('C:/dev/Output/y_train.csv', index=False)
y_test.to_csv('C:/dev/Output/y_test.csv', index=False)
