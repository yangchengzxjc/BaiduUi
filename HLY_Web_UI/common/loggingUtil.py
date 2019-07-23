import logging


class Logging:
    logger = logging.getLogger('root')

    @classmethod
    def set_logger_name(cls, name):
        cls.logger.name = name

    @classmethod
    def get_logger(cls):
        return cls.logger
