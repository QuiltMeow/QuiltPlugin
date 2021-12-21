SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for qp_login
-- ----------------------------
DROP TABLE IF EXISTS `qp_login`;
CREATE TABLE `qp_login` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `uuid` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `ip` varchar(255) NOT NULL,
  `lastlogin` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `lastlogout` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  KEY `uuid` (`uuid`(191)),
  KEY `name` (`name`(191)),
  KEY `ip` (`ip`(191)),
  KEY `lastlogin` (`lastlogin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of qp_login
-- ----------------------------