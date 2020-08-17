package com.hazz.aipick.mvp.model.bean;

public class CoinBean {

    /**
     * name : BTC
     * nameen : Bitcoin
     * time : 2019-08-31
     * desc : 比特币（Bitcoin，简称BTC）是目前使用最为广泛的一种数字货币，它诞生于2009年1月3日，是一种点对点（P2P）传输的数字加密货币，总量2100万枚。比特币网络每10分钟释放出一定数量币，预计在2140年达到极限。比特币被投资者称为“数字黄金”。比特币依据特定算法，通过大量的计算产生，不依靠特定货币机构发行，其使用整个P2P网络中众多节点构成的分布式数据库来确认并记录所有的交易行为，并使用密码学设计确保货币流通各个环节安全性，可确保无法通过大量制造比特币来人为操控币值。基于密码学的设计可以使比特币只能被真实拥有者转移、支付及兑现。同样确保了货币所有权与流通交易的匿名性。
     * circulation : 1673.82
     * issue : 2100万
     * cmplink : https://bitcoin.org/en/
     * chainlink : https://www.blockchain.com/explorer
     */

    private String name;
    private String nameen;
    private String time;
    private String desc;
    private String circulation;
    private String issue;
    private String cmplink;
    private String chainlink;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameen() {
        return nameen;
    }

    public void setNameen(String nameen) {
        this.nameen = nameen;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCirculation() {
        return circulation;
    }

    public void setCirculation(String circulation) {
        this.circulation = circulation;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getCmplink() {
        return cmplink;
    }

    public void setCmplink(String cmplink) {
        this.cmplink = cmplink;
    }

    public String getChainlink() {
        return chainlink;
    }

    public void setChainlink(String chainlink) {
        this.chainlink = chainlink;
    }

}
