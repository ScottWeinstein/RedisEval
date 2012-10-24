reds = require('reds')
byline = require('byline')
fs = require 'fs'
redis = require('redis')

eqSearch = reds.createSearch('equities')
client = eqSearch.client
client.on "error", (err) -> console.error "Redis Error " + err


indexer = (line) ->
  sp = line.split('|')
  if /^\w+$/.test(sp[1])
    desc =         
      sectype: 'Equity'
      description: sp[0]
    client.hmset(sp[1], desc,  (err, res) -> if err then console.error(err + res))
    eqSearch.index(sp[0] + " " + sp[1], sp[1])

indexFile = (filePath, cb = () -> console.log("complete")) ->
  console.log(filePath)
  rs = byline(fs.createReadStream(filePath))
  rs.on('data', indexer)
  rs.on('end', cb)
  rs.on('error', (ex) -> console.error(ex))
  


exit = ()->
  console.log("done")
  client.quit()
  process.exit()

index2 = () -> 
  indexFile("Equity_Common_Stock_20121019/Equity_Common_Stock_02_20121019.txt", exit)
indexFile("Equity_Common_Stock_20121019/Equity_Common_Stock_01_20121019.txt", index2)
