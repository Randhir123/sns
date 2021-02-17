resource "aws_sqs_queue" "config_updates" {
  name = "config-updates-listener"
}

resource "aws_sns_topic_subscription" "sqs" {
  topic_arn = aws_sns_topic.config_updates.arn
  protocol  = "sqs"
  endpoint  = aws_sqs_queue.config_updates.arn
}

resource "aws_sqs_queue_policy" "test" {
  queue_url = aws_sqs_queue.config_updates.id

  policy = <<POLICY
{
  "Version": "2012-10-17",
  "Id": "sqspolicy",
  "Statement": [
    {
      "Sid": "First",
      "Effect": "Allow",
      "Principal": "*",
      "Action": "sqs:SendMessage",
      "Resource": "${aws_sqs_queue.config_updates.arn}",
      "Condition": {
        "ArnEquals": {
          "aws:SourceArn": "${aws_sns_topic.config_updates.arn}"
        }
      }
    }
  ]
}
POLICY
}