# Publish notification to SNS and subscribe for notification in SQS

Run terraform to create infrastucture

    cd terraform
    terraform init
    terraform plan
    terraform apply
  
Hit the endpoint to publish a message to SNS topic and see the response received from SQS queue.
