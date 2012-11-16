completer = require('redis-completer')
byline = require('byline')
fs = require 'fs'
redis = require('redis')
async  = require('async')
_ = require('underscore')

completer.applicationPrefix('eq')
client = redis.createClient()
client.on "error", (err) -> console.error "Redis Error " + err

indexer = (line, instrument) ->
  sp = line.split('|')
  key = sp[1]
  text = sp[0]
  if /^\w+/.test(key) and /^\w+/.test(text)
    desc =         
      sectype: instrument
      description: text
      symbol: key
    redisKey = "Symbol:#{instrument}:#{key}"
    client.exists redisKey, (err, exists) ->
      if not exists
        proc = (text, key, redisKey, desc) ->
            client.hmset redisKey, desc,  (err, res) -> 
                if err then console.error(err + res)
            completer.addCompletions(text, key)
            completer.addCompletions(key , key, 100)
        proc(text, key, redisKey, desc)

indexFile = (filePath, cb = () -> console.log("complete")) ->
  instrument = 
  if /Equity_Common_Stock/.test(filePath)
     "eq"
  else if /Equity_Equity_Option/.test(filePath)
    "option"
  else if /Equity_Index_WRT/.test(filePath)
    "index"
  else
    null
  if instrument
    console.log("#{instrument} = #{filePath}")
    rs = byline(fs.createReadStream(filePath))
    rs.on('data', (line) -> indexer(line, instrument))
    rs.on('end', cb)
    rs.on('error', (ex) -> console.error(ex))
  


exit = ()->
  console.log("done")
  client.quit()
  process.exit()

fs.readdir "d", (err, files) ->
  files = _.map(files, (f) -> "d/#{f}")
  async.forEach(files, indexFile, exit)
# index2 = () -> 
#   indexFile("d/Equity_Common_Stock_02_20121019.txt", () -> indexFile("d/Equity_Index_WRT_20121020.txt", exit))
# indexFile("d/Equity_Common_Stock_01_20121019.txt", index2)

