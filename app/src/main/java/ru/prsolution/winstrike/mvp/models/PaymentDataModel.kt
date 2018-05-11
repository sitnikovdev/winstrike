package ru.prsolution.winstrike.mvp.models

class PaymentDataModel {

    //static let sharedInstance = PidTimeDate()

     init { }

    var pid: String = ""
    var pids: Set<String> = HashSet()
    var date: String  = "" {
        didSet {
            isDatesValid = false
        }
    }

    var startAt: String  = "" {
        didSet {
            if let t = DateTransform().transformFromJSON(startAt) {
                startDate = t
                UDManager.save(time: t)
            }
        }
    }

    var endAt: String = "" {
        didSet {
            if let t = DateTransform().transformFromJSON(endAt) {
                endDate = t
            }
        }
    }

    func setDate(datetime: Date) {
        if let d = DateTransform().transformToJSON(datetime) {
            date = d
        }
    }

    var startDate: Date? {
        didSet {
            isDateValid()
        }
    }

    var endDate: Date? {
        didSet {
            isDateValid()
        }
    }

    var isDatesValid: Bool = false {
        didSet {
            print( "dates: valid \(isDatesValid), startAt: \(startAt), endAt: \(endAt)" )
        }
    }

    private func isDateValid() {
        if let start = startDate, let end = endDate {
            let current = Date()
            isDatesValid = start < end && start >= current
        }
    }

    // MARK: callers is seatpickerpresenter, paidplacesvc and chooseatvc
    func clear() {
        pids = []
        date = ""
        startAt = ""
        endAt = ""
        endDate = nil
        startDate = nil
        isDatesValid = false
    }
}

