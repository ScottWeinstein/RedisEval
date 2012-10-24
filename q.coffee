reds = require('reds')
redis = require('redis')
completer = require('redis-completer')


completer.applicationPrefix('eq')
eqSearch = reds.createSearch('equities')

client = client = eqSearch.client #redis.createClient()
client.on "error", (err) -> console.error "Redis Error " + err

start = +new Date()
end = 0

# eqSearch.query(query = "ib").end (err, ids) ->
#   end = +new Date()
#   console.log (end-start)
#   throw err  if err
#   console.log "Search results for \"%s\":", query
#   printRes = (id) ->
#     idn = id
#     cb = (err, obj) -> console.log(idn + "=" + JSON.stringify(obj))
#     client.hgetall(id, cb)

#   for id in ids
#     printRes(id)
#   console.log("\n\n")  



completer.search "ABN", 10, (err, res) ->
  end = +new Date()
  console.log (end-start)
  throw err if err 
  for item in res
    console.log item

    
