-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        5.7.17-log - MySQL Community Server (GPL)
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- 导出 journal_info 的数据库结构
CREATE DATABASE IF NOT EXISTS `journal_info` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `journal_info`;

-- 导出  表 journal_info.journal_info 结构
CREATE TABLE IF NOT EXISTS `journal_info` (
  `j_number` varchar(50) NOT NULL COMMENT '论文编号',
  `j_title` varchar(200) DEFAULT NULL COMMENT '论文标题',
  `j_abstract` text COMMENT '摘要',
  `j_citation_frequency` int(11) DEFAULT '0' COMMENT '总被引频次',
  `j_others_citation` int(11) DEFAULT '0' COMMENT '他引频次',
  `j_pages` varchar(50) DEFAULT NULL COMMENT '原始记录页',
  `j_class_No` varchar(50) DEFAULT NULL COMMENT '分类号',
  `j_year` varchar(50) DEFAULT NULL COMMENT '发表年',
  `j_orgin` varchar(100) DEFAULT NULL COMMENT '来源',
  `j_page2` double unsigned DEFAULT '0' COMMENT '转变后的页数',
  PRIMARY KEY (`j_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='期刊文献基本信息表';

-- 数据导出被取消选择。
-- 导出  表 journal_info.paper_address 结构
CREATE TABLE IF NOT EXISTS `paper_address` (
  `j_number` varchar(50) NOT NULL COMMENT '论文编号',
  `j_address` varchar(200) NOT NULL COMMENT '发表单位',
  PRIMARY KEY (`j_number`,`j_address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='论文-作者单位关系';

-- 数据导出被取消选择。
-- 导出  表 journal_info.paper_author 结构
CREATE TABLE IF NOT EXISTS `paper_author` (
  `j_number` varchar(50) NOT NULL COMMENT '论文编号',
  `j_author` varchar(200) NOT NULL COMMENT '作者',
  PRIMARY KEY (`j_number`,`j_author`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='论文-作者关系';

-- 数据导出被取消选择。
-- 导出  表 journal_info.paper_fund 结构
CREATE TABLE IF NOT EXISTS `paper_fund` (
  `j_number` varchar(50) NOT NULL COMMENT '论文编号',
  `j_fund` varchar(100) NOT NULL COMMENT '基金名',
  PRIMARY KEY (`j_number`,`j_fund`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='论文-基金关系';

-- 数据导出被取消选择。
-- 导出  表 journal_info.paper_keywords 结构
CREATE TABLE IF NOT EXISTS `paper_keywords` (
  `j_number` varchar(50) NOT NULL,
  `keyword` varchar(100) NOT NULL,
  PRIMARY KEY (`keyword`,`j_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='论文-关键字表';

-- 数据导出被取消选择。
-- 导出  表 journal_info.paper_references 结构
CREATE TABLE IF NOT EXISTS `paper_references` (
  `j_number` varchar(50) NOT NULL COMMENT '论文编号',
  `r_number` varchar(50) NOT NULL,
  `r_title` tinytext COMMENT '引文题名',
  `r_journal` varchar(200) DEFAULT NULL COMMENT '期刊来源',
  `r_year` varchar(100) DEFAULT NULL COMMENT '发表年',
  `r_pages` varchar(20) DEFAULT NULL COMMENT '页',
  `r_type` int(11) unsigned DEFAULT NULL COMMENT '0参考文献,1引用文献',
  PRIMARY KEY (`j_number`,`r_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='论文-参考文献表';

-- 数据导出被取消选择。
-- 导出  过程 journal_info.proce_core_author 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `proce_core_author`()
    COMMENT '查询候选核心作者'
BEGIN

select distinct j_author as 作者
from paper_author as A,journal_info as B
where A.j_number = B.j_number and j_author in(
     select j_author as 作者
     from paper_author 
     group by j_author 
     having count(*)>=(
           select  0.749*sqrt(count(*)) as 普赖斯指数 
           from paper_author  
           group by j_author 
           order by 普赖斯指数 DESC limit 1
			  )
	  )
order by j_author,j_citation_frequency DESC;

END//
DELIMITER ;

-- 导出  表 journal_info.references_author 结构
CREATE TABLE IF NOT EXISTS `references_author` (
  `j_number` varchar(50) NOT NULL,
  `r_number` varchar(50) NOT NULL COMMENT '标题',
  `r_author` varchar(300) NOT NULL COMMENT '作者',
  PRIMARY KEY (`r_number`,`r_author`,`j_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='参考文献的作者关系表';

-- 数据导出被取消选择。
-- 导出  表 journal_info.user_record 结构
CREATE TABLE IF NOT EXISTS `user_record` (
  `user_id` varchar(50) DEFAULT NULL,
  `record_start` int(11) DEFAULT NULL,
  `record_end` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户访问记录';

-- 数据导出被取消选择。
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
