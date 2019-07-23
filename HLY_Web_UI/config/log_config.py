log_config = {
    'version': 1,
    'formatters': {
        'detailed': {
            'class': 'logging.Formatter',
            'format': '%(asctime)s %(name)-15s %(levelname)s: %(message)s'
        }
    },
    'handlers': {
        'console': {
            'class': 'logging.StreamHandler',
            'level': 'WARNING',
            'formatter': 'detailed',
        },
        'file': {
            'class': 'logging.FileHandler',
            'filename': './logs/zhishinet.log',
            'mode': 'w',
            'formatter': 'detailed',
            'level': 'INFO',
            'encoding': 'utf-8'
        }
    },
    'root': {
        'level': 'WARNING',
        'handlers': ['console', 'file']
    },
}
